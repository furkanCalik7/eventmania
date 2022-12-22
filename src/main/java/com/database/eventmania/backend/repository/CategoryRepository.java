package com.database.eventmania.backend.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class CategoryRepository extends BaseRepository {
    public CategoryRepository(){
        super.connect();
    }

    public boolean addCategory(Long eventId, String categoryName,
                               String categoryDescription, Integer capacity,
                               Integer price) throws SQLException {
        Connection conn = super.getConnection();
        if (conn != null){
            String query = "INSERT INTO Category (ticketed_event_id, category_name, category_description, capacity, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Math.toIntExact(eventId));
            stmt.setString(2, categoryName);
            stmt.setString(3, categoryDescription);
            stmt.setInt(4, capacity);
            stmt.setDouble(5, price);
            stmt.executeUpdate();
            return true;
        }
        else{throw new SQLException("Connection to the database could not be established");}

    }

}
