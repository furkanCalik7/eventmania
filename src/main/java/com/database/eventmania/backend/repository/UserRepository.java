package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.entity.enums.Gender;
import com.database.eventmania.backend.model.EventModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public class UserRepository extends BaseRepository {
    public UserRepository() {
        super.connect();
    }

    // TODO: connect to database and test the code
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
            // TODO: user executeUpdate rahter executeQuery since its throwing "result is returned"
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


        query = "SELECT * " +
                "FROM Event E " +
                "LEFT OUTER JOIN Location L ON E.event_id = L.event_id " +
                "LEFT OUTER JOIN event_type ET ON E.event_id = ET.event_id " +
                "WHERE event_id = (SELECT * FROM join_event WHERE user_id = ?)";
        stmt = conn.prepareStatement(query);
        stmt.setLong(1, userId);
        rs = stmt.executeQuery();

        ArrayList<EventModel> events = new ArrayList<>();
        while (rs.next()) {
            EventModel event = new EventModel();

            event.setEventId(rs.getLong("event_id"));
            event.setTitle(rs.getString("event_name"));
            event.setVenueLocation(rs.getString("location_name"));
            event.setAddress(rs.getString("address_description"));
            event.setStartdate(String.valueOf(rs.getDate("start_date").toLocalDate()));
            event.setEnddate(String.valueOf(rs.getDate("end_date").toLocalDate()));
            event.setEventDescription(rs.getString("event_description"));
            event.setLocationType(rs.getString("location_type"));

            events.add(event);
        }

        return events;
    }
}
