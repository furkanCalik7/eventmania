package com.database.eventmania.backend.repository;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@Repository
public class AccountRepository extends BaseRepository {
    public HashMap<String, String> getAccountByEmail(String email) throws SQLException {
        Connection conn = super.getConnection();

        if (conn != null) {
            String query = "SELECT * FROM account_type WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                HashMap<String, String> account = new HashMap<>();
                account.put("email", rs.getString("email"));
                account.put("password", rs.getString("hash_password"));
                account.put("type", rs.getString("type"));
                return account;
            }
        }

        throw new SQLException("Connection to the database failed");
    }

}
