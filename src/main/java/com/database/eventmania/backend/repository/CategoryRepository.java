package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.Category;
import com.database.eventmania.backend.model.CategoryModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Repository
public class CategoryRepository extends BaseRepository {
    public CategoryRepository() {
        super.connect();
    }

    public boolean addCategory(Long eventId, String categoryName,
                               String categoryDescription, Integer capacity,
                               Integer price) throws SQLException {
        Connection conn = super.getConnection();
        if (conn != null) {
            String query = "INSERT INTO Category (ticketed_event_id, category_name, category_description, capacity, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Math.toIntExact(eventId));
            stmt.setString(2, categoryName);
            stmt.setString(3, categoryDescription);
            stmt.setInt(4, capacity);
            stmt.setDouble(5, price);
            stmt.executeUpdate();
            return true;
        } else {
            throw new SQLException("Connection to the database could not be established");
        }

    }

    public ArrayList<Category> getCategoriesByEventId(Long valueOf) throws SQLException {
        Connection conn = super.getConnection();
        if (conn != null) {
            String query = "SELECT * FROM Category WHERE ticketed_event_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Math.toIntExact(valueOf));
            ResultSet rs = stmt.executeQuery();
            //return category arraylist
            ArrayList<Category> categories = new ArrayList<>();
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryName(rs.getString("category_name"));
                category.setCategoryDescription(rs.getString("category_description"));
                category.setCapacity(rs.getInt("capacity"));
                category.setPrice((int)(rs.getDouble("price")));
                categories.add(category);
            }
            return categories;
        } else {
            throw new SQLException("Connection to the database could not be established");
        }
    }

    //Capacity check / Capacity Control is done here
    public ArrayList<CategoryModel> getCategoriesByEventIdWithCapacityCheck(Long eventId) throws SQLException {
        Connection conn = super.getConnection();
        if (conn != null) {
            String query = "SELECT * FROM Category WHERE ticketed_event_id = ? AND capacity > 0";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Math.toIntExact(eventId));
            ResultSet rs = stmt.executeQuery();
            //These are the requested category's

            rs.next();
            Long ticketedEventId = rs.getLong("ticketed_event_id");
            String category_name = rs.getString("category_name");

            //get remaining capacity
            String query2 = "SELECT COUNT(*) AS boughtTicketCount, category_name FROM Ticket WHERE ticketed_event_id = ? GROUP BY ticketed_event_id, category_name";
            PreparedStatement stmt2 = conn.prepareStatement(query2);
            stmt2.setInt(1, Math.toIntExact(ticketedEventId));
            ResultSet rs2 = stmt2.executeQuery();
            //put results in map
            HashMap<String, Integer> remainingCapacityMap = new HashMap<>();

            while(rs2.next()){
                remainingCapacityMap.put(rs2.getString("category_name"), rs2.getInt("boughtTicketCount"));
            }
            //return category arraylist
            ArrayList<CategoryModel> categories = new ArrayList<>();
            do {
                if( rs.getInt("capacity") - remainingCapacityMap.get(rs.getString("category_name")) <= 0){
                    continue;
                }
                CategoryModel category = new CategoryModel();
                category.setName(rs.getString("category_name"));
                category.setDesc(rs.getString("category_description"));
                category.setCapacity(Integer.toString(rs.getInt("capacity")));
                category.setPrice((Double.toString(rs.getDouble("price"))));
                category.setRemainingCapacity(rs.getInt("capacity") - remainingCapacityMap.get(rs.getString("category_name")) );
                categories.add(category);
            }while (rs.next());
            return categories;
        } else {
            throw new SQLException("Connection to the database could not be established");
        }
    }
}
