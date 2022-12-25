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

    //TODO: check the age constraint or others
    public boolean buyTicket(Long eventId, Long userId, String categoryName, String purchaseType) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null) {
            String query = "INSERT INTO Ticket (ticketed_event_id, user_id, transaction_date, category_name, purchase_type) VALUES (?, ?, ?, ?, ?)";
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
