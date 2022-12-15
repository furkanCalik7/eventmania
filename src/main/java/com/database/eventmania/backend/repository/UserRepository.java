package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.BasicUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public boolean saveUser(String username, String password) throws SQLException {
        Connection conn = super.getConnection();
        // TODO: fix here
        if (conn != null) {
            String query = "INSERT INTO basicuser (hash_password, email, wallet_id, first_name, last_name) " +
                    "VALUES (?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, password);
            stmt.setString(2, username);
            stmt.setInt(3, 0);
            stmt.setString(4, "furkan");
            stmt.setString(5, "calik");
            ResultSet rs = stmt.executeQuery();
            return true;
        }
        return false;
    }
}
