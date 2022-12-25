package com.database.eventmania.backend.repository;

import org.springframework.stereotype.Repository;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDateTime;

@Repository
public class TicketRepository extends BaseRepository {
    public TicketRepository(){
        super.connect();
    }
    public boolean createTicket(Long eventId, Long userId, String categoryName, String purchaseType) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null) {
            String query = "INSERT INTO Ticket (ticketed_event_id, user_id, transaction_date, category_name, purchase_type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Math.toIntExact(eventId));
            stmt.setInt(2, Math.toIntExact(userId));
            stmt.setString(4, categoryName);
            stmt.setString(5, purchaseType);
            stmt.executeUpdate();
            return true;
        }
        throw new SQLException("Connection to the database could not be established");
    }

    public boolean buyTicket(Long eventId, String email, String categoryName, String purchaseType) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null) {
            String query = "SELECT organization_id FROM account_with_type WHERE email = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            rs.next();
            Long userId = rs.getLong("organization_id");

            //Date check is implemented here
            query = "SELECT current_state FROM event WHERE event_id = ?";
            statement = conn.prepareStatement(query);
            statement.setInt(1, Math.toIntExact(eventId));
            rs = statement.executeQuery();
            rs.next();
            String currentState = rs.getString("current_state");
            if(currentState.toUpperCase().equals("ONGOING") || currentState.toUpperCase().equals("FINISHED")){
                throw new SQLException("Event is either finished or ongoing");
            }

            query = "INSERT INTO Ticket (ticketed_event_id, user_id, transaction_date, category_name, purchase_type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Math.toIntExact(eventId));
            stmt.setInt(2, Math.toIntExact(userId));
            //get current time as timestamp
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);

            stmt.setTimestamp(3, timestamp);
            stmt.setString(4, categoryName);
            stmt.setString(5, purchaseType);
            stmt.executeUpdate();

            //check if user can join by checking the minimum age constraint
            String ageQuery = "SELECT minimum_age FROM Event WHERE event_id = ?";
            PreparedStatement ageStmt = conn.prepareStatement(ageQuery);
            ageStmt.setInt(1, Math.toIntExact(eventId));
            ResultSet ageResult = ageStmt.executeQuery();
            if(ageResult.next()){
                int minimumAge = ageResult.getInt("minimum_age");
                String userAgeQuery = "SELECT date_of_birth FROM basicuser WHERE user_id = ?";
                PreparedStatement userAgeStmt = conn.prepareStatement(userAgeQuery);
                userAgeStmt.setInt(1, Math.toIntExact(userId));
                ResultSet userAgeResult = userAgeStmt.executeQuery();
                if(userAgeResult.next()){
                    Date birthDate = userAgeResult.getDate("birth_date");
                    if(birthDate != null){
                        int userAge = now.getYear() - birthDate.toLocalDate().getYear();
                        if(userAge < minimumAge){
                            return false;
                        }
                    }
                }
            }
            String query2 = "INSERT INTO join_event (event_id, user_id) VALUES (?, ?)";
            PreparedStatement stmt2 = conn.prepareStatement(query2);
            stmt2.setInt(1, Math.toIntExact(eventId));
            stmt2.setInt(2, Math.toIntExact(userId));
            stmt2.executeUpdate();
            return true;
        }
        throw new SQLException("Connection to the database could not be established");
    }
}
