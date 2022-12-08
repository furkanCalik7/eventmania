package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository extends BaseRepository {
    public UserRepository() {
        super.connect();
    }

    // TODO: connect to database and test the code
    public User getUserById(Long userId) throws SQLException {
        Connection conn = super.getConnection();
        if (conn != null) {
            String query = "SELECT * FROM User WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setLong(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return convertQueryResultToUser(rs);
            }
        }
        return null;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        // TODO: IMPLEMENT
        return null;
    }
}
