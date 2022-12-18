package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.Rating;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class RatingRepository extends BaseRepository {
    public RatingRepository(){
        super.connect();
    }

    public Rating getRatingById(Integer ratingId) throws SQLException {
        Connection conn = super.getConnection();
        if (conn != null) {
            String query = "SELECT * FROM Rating WHERE rating_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, ratingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                return convertQueryResultToRating(rs);
            return null;
        }
        throw new SQLException("Connection to the database failed");
    }

    public boolean createRating(Integer eventId, Integer userId, Integer point, String topic, String ratingComment) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "INSERT INTO Rating (event_id, user_id, point, topic, rating_comment) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            stmt.setInt(3, point);
            stmt.setString(4, topic);
            stmt.setString(5, ratingComment);
            stmt.executeUpdate();
            return true;
        }
        return false;
    }

    public boolean deleteRating(Integer ratingId) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "DELETE FROM Rating WHERE rating_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, ratingId);
            stmt.executeUpdate();
            return true;
        }
        return false;
    }

    public boolean updateRating(Integer eventId, Integer ratingId, Integer point, String topic, String ratingComment) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "UPDATE Rating " +
                            "SET point = ?, topic = ?, rating_comment = ? " +
                            "WHERE rating_id = ? AND event_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, point);
            stmt.setString(2, topic);
            stmt.setString(3, ratingComment);
            stmt.setInt(4, ratingId);
            stmt.setInt(5, eventId);
            stmt.executeUpdate();
            return true;
        }
        return false;
    }
}
