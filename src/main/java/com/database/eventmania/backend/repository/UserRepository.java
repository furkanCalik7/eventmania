package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.User;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class UserRepository extends BaseRepository {
    public UserRepository() {
        super.connect();

    }
    public User getUserById(Long userId) throws SQLException {
        Connection conn = super.getConnection();
        if (conn != null) {
            String query = "SELECT * FROM User WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement();
            stmt.setLong(1, userId);

            ResultSet rs = stmt.executeQuery(query);

        }

        return null;

    }
}
