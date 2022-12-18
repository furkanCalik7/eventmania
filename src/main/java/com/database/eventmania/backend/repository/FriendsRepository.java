package com.database.eventmania.backend.repository;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class FriendsRepository extends BaseRepository {
    public FriendsRepository(){
        super.connect();
    }

    public boolean addFriend(Long userId, Long friendId) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null) {
            String query = "INSERT INTO Friends (user_id, friend_id) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Math.toIntExact(userId));
            stmt.executeUpdate();
            return true;
        }
        return false;
    }

    /*
    Deletes the row with the given ID. The ID can belong either to friend_id column or the user_id column.
     */
    public boolean deleteFriend(Long userId) throws SQLException{
        Connection conn = super.getConnection();
        if (conn != null){
            String query = " DELETE FROM Friends WHERE user_id = ? OR friend_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Math.toIntExact(userId));
            stmt.setInt(2, Math.toIntExact(userId));
            stmt.executeUpdate();
            return true;
        }
        return false;
    }
}
