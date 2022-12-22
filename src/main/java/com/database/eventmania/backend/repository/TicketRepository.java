package com.database.eventmania.backend.repository;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
    public boolean buyTicket(Long eventId, Long userId, String categoryName, String purchaseType) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null) {
            String query = "INSERT INTO Ticket (ticketed_event_id, user_id, transaction_date, category_name, purchase_type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Math.toIntExact(eventId));
            stmt.setInt(2, Math.toIntExact(userId));
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(4, categoryName);
            stmt.setString(5, purchaseType);
            stmt.executeUpdate();
            return true;
        }
        throw new SQLException("Connection to the database could not be established");
    }
}
