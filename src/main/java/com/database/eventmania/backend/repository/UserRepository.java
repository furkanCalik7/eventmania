package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.entity.enums.Gender;
import com.database.eventmania.backend.model.EventModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

@Repository
public class UserRepository extends BaseRepository {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

    public UserRepository() {
        super.connect();
    }

    public BasicUser getUserById(Long userId) throws SQLException {
        Connection conn = super.getConnection();

        if (conn != null) {
            String query = "SELECT * FROM BasicUser WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setLong(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                return convertQueryResultToUser(rs);
            return null;
        }

        throw new SQLException("Connection to the database failed");
    }

    public BasicUser getUserByEmailAndPassword(String email, String hashedPassword) throws SQLException {
        Connection conn = super.getConnection();

        if (conn != null) {
            String query = "SELECT * FROM BasicUser WHERE email = ? AND hash_password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return convertQueryResultToUser(rs);
            return null;
        }

        throw new SQLException("Connection to the database failed");
    }

    public BasicUser getUserByEmail(String email) throws SQLException {
        Connection conn = super.getConnection();

        if (conn != null) {
            String query = "SELECT * FROM BasicUser WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return convertQueryResultToUser(rs);
            return null;
        }
        throw new SQLException("Connection to the database failed");
    }

    public boolean saveUser(String email, String hashPassword,
                            String firstName, String lastName, Gender gender,
                            String phoneNumber, LocalDate dob) throws SQLException {
        Connection conn = super.getConnection();
        if (conn != null) {
            String query = "INSERT INTO BasicUser (hash_password, email, first_name, last_name, gender, phone_number, date_of_birth) " +
                    "VALUES (?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, hashPassword);
            stmt.setString(2, email);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, gender.name());
            stmt.setString(6, phoneNumber);
            stmt.setDate(7, Date.valueOf(dob));
            stmt.executeUpdate();
            return true;
        }
        return false;
    }

    public ArrayList<BasicUser> getAllUsers() throws SQLException {
        Connection conn = super.getConnection();
        if (conn != null) {
            String query = "SELECT * FROM BasicUser";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            ArrayList<BasicUser> users = new ArrayList<>();
            while (rs.next()) {
                users.add(convertQueryResultToUser(rs));
            }
            return users;
        }
        throw new SQLException("Connection to the database failed");
    }

    public boolean deleteUserById(Long userId) throws SQLException {
        Connection conn = super.getConnection();

        if (conn != null) {
            String query = "DELETE FROM BasicUser WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, userId);
            stmt.executeUpdate();
            return true;
        }
        return false;
    }

    public ArrayList<EventModel> listJoinedEvents(String userEmail) throws SQLException {
        Connection conn = super.getConnection();

        if (conn == null)
            throw new SQLException("Connection to the database failed");

        Long userId;

        // search for the user in BasicUser table
        String query = "SELECT user_id FROM BasicUser WHERE email = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, userEmail);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next())
            throw new SQLException("User not found");
        else
            userId = rs.getLong("user_id");

        query = "SELECT DISTINCT E.event_id, E.event_name, E.start_date, E.end_date, E.description, L.location_name, L.address_description " +
                "FROM Event E " +
                "LEFT OUTER JOIN Location L ON E.event_id = L.event_id " +
                "LEFT OUTER JOIN event_type ET ON E.event_id = ET.event_id " +
                "WHERE (E.event_id = ANY (SELECT JE.event_id FROM join_event JE WHERE user_id = ?)) AND E.end_date < ?";
        stmt = conn.prepareStatement(query);
        LocalDateTime now = LocalDateTime.now();
        Timestamp currentTimestamp = Timestamp.valueOf(now);


        stmt.setLong(1, userId);
        stmt.setTimestamp(2, currentTimestamp);
        rs = stmt.executeQuery();

        ArrayList<EventModel> events = new ArrayList<>();
        while (rs.next()) {
            EventModel event = new EventModel();

            event.setEventId(rs.getLong("event_id"));
            event.setTitle(rs.getString("event_name"));
            if(rs.getString("location_name") != null) {
                event.setVenueLocation(rs.getString("location_name"));
                event.setAddress(rs.getString("address_description"));
            }
            else{
                event.setVenueLocation("Online");
                event.setAddress("Online");
            }
            event.setStartdate(String.valueOf(rs.getDate("start_date").toLocalDate().format(formatter)));
            event.setEnddate(String.valueOf(rs.getDate("end_date").toLocalDate().format(formatter)));
            event.setEventDescription(rs.getString("description"));
            events.add(event);
        }

        return events;
    }

    public ArrayList<EventModel> listFutureEvents(String userEmail) throws SQLException{
        Connection conn = super.getConnection();

        if (conn == null)
            throw new SQLException("Connection to the database failed");

        Long userId;

        // search for the user in BasicUser table
        String query = "SELECT user_id FROM BasicUser WHERE email = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, userEmail);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next())
            throw new SQLException("User not found");
        else
            userId = rs.getLong("user_id");

        query = "SELECT DISTINCT E.event_id, E.event_name, E.start_date, E.end_date, E.description, L.location_name, L.address_description " +
                "FROM Event E " +
                "LEFT OUTER JOIN Location L ON E.event_id = L.event_id " +
                "LEFT OUTER JOIN event_type ET ON E.event_id = ET.event_id " +
                "WHERE (E.event_id = ANY (SELECT JE.event_id FROM join_event JE WHERE user_id = ?)) AND E.start_date > ?";
        stmt = conn.prepareStatement(query);
        LocalDateTime now = LocalDateTime.now();
        Timestamp currentTimestamp = Timestamp.valueOf(now);

        stmt.setLong(1, userId);
        stmt.setTimestamp(2, currentTimestamp);
        rs = stmt.executeQuery();

        ArrayList<EventModel> events = new ArrayList<>();
        while (rs.next()) {
            EventModel event = new EventModel();

            event.setEventId(rs.getLong("event_id"));
            event.setTitle(rs.getString("event_name"));
            if(rs.getString("location_name") != null) {
                event.setVenueLocation(rs.getString("location_name"));
                event.setAddress(rs.getString("address_description"));
            }
            else{
                event.setVenueLocation("Online");
                event.setAddress("Online");
            }
            event.setStartdate(String.valueOf(rs.getDate("start_date").toLocalDate().format(formatter)));
            event.setEnddate(String.valueOf(rs.getDate("end_date").toLocalDate().format(formatter)));
            event.setEventDescription(rs.getString("description"));
            events.add(event);
        }

        return events;
    }

    public ArrayList<EventModel> getOrganizedEvents(String email) throws SQLException{
        Connection conn = super.getConnection();

        if (conn == null)
            throw new SQLException("Connection to the database failed");

        Long userId;

        // search for the user in BasicUser table
        String query = "SELECT user_id FROM BasicUser WHERE email = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next())
            throw new SQLException("User not found");
        else
            userId = rs.getLong("user_id");

        query = "SELECT DISTINCT E.event_id, E.event_name, E.start_date, E.end_date, E.description, L.location_name, L.address_description " +
                "FROM Event E " +
                "LEFT OUTER JOIN Location L ON E.event_id = L.event_id " +
                "LEFT OUTER JOIN unticketedevent UE ON UE.event_id = E.event_id " +
                "WHERE UE.user_id = ?";
        stmt = conn.prepareStatement(query);
        stmt.setLong(1, userId);
        rs = stmt.executeQuery();

        ArrayList<EventModel> events = new ArrayList<>();
        while (rs.next()) {
            EventModel event = new EventModel();

            event.setEventId(rs.getLong("event_id"));
            event.setTitle(rs.getString("event_name"));
            if(rs.getString("location_name") != null) {
                event.setVenueLocation(rs.getString("location_name"));
                event.setAddress(rs.getString("address_description"));
            }
            else{
                event.setVenueLocation("Online");
                event.setAddress("Online");
            }
            event.setStartdate(String.valueOf(rs.getDate("start_date").toLocalDate().format(formatter)));
            event.setEnddate(String.valueOf(rs.getDate("end_date").toLocalDate().format(formatter)));
            event.setEventDescription(rs.getString("description"));
            events.add(event);
        }

        return events;
    }
    //Join unticketed event
    public boolean joinUnticketedEvent(Long eventId, String email) throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null) {
            throw new SQLException("Connection to the database failed");
        }

        //gets the account's user id
        String query = "SELECT organization_id FROM account_with_type WHERE email = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
        rs.next();
        Long userId = rs.getLong("organization_id");


        //2.c AGE CHECKAGECONSTRAINT AGE AGE check if user can join by checking the minimum age constraint
        String ageQuery = "SELECT minimum_age FROM Event WHERE event_id = ?";
        PreparedStatement ageStmt = conn.prepareStatement(ageQuery);
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        ageStmt.setInt(1, Math.toIntExact(eventId));
        ResultSet ageResult = ageStmt.executeQuery();
        if(ageResult.next()){
            int minimumAge = ageResult.getInt("minimum_age");
            String userAgeQuery = "SELECT date_of_birth FROM basicuser WHERE user_id = ?";
            PreparedStatement userAgeStmt = conn.prepareStatement(userAgeQuery);
            userAgeStmt.setInt(1, Math.toIntExact(userId));
            ResultSet userAgeResult = userAgeStmt.executeQuery();
            if(userAgeResult.next()){
                Date birthDate = userAgeResult.getDate("date_of_birth");
                if(birthDate != null){
                    int userAge = now.getYear() - birthDate.toLocalDate().getYear();
                    if(userAge < minimumAge){
                        return false;
                    }
                }
            }
        }
        query = "INSERT INTO join_event (event_id, user_id) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setLong(1, eventId);
        stmt.setLong(2, userId);
        stmt.executeUpdate();
        return true;
    }

    public boolean isUserInEvent(Long eventId, String email) throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null) {
            throw new SQLException("Connection to the database failed");
        }
        String query = "SELECT organization_id FROM account_with_type WHERE email = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
        rs.next();
        Long userId = rs.getLong("organization_id");

        query = "SELECT user_id FROM join_event WHERE event_id = ? AND user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setLong(1, eventId);
        stmt.setLong(2, userId);
        ResultSet rs2 = stmt.executeQuery();
        if(rs2.next()){
            return true;
        }
        return false;
    }
    public String getEventState(Long eventId) throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null) {
            throw new SQLException("Connection to the database failed");
        }
        String query = "SELECT current_state FROM Event WHERE event_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setLong(1, eventId);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            return rs.getString("current_state");
        }
        return null;
    }

    public boolean withdrawFromEvent(Long eventId, String email) throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null) {
            throw new SQLException("Connection to the database failed");
        }
        String query = "SELECT organization_id FROM account_with_type WHERE email = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
        rs.next();
        Long userId = rs.getLong("organization_id");

        query = "DELETE FROM join_event WHERE event_id = ? AND user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setLong(1, eventId);
        stmt.setLong(2, userId);
        stmt.executeUpdate();
        return true;
    }

}
