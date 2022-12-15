package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.Admin;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AdminRepository extends BaseRepository {
    public AdminRepository() {
        super.connect();
    }

    public Admin getAdminById(Long adminId) throws SQLException {
        Connection conn = super.getConnection();
        if (conn != null) {
            String query = "SELECT * FROM Admin WHERE admin_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setLong(1, adminId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                return convertQueryResultToAdmin(rs);
        }
        return null;
    }

    public Admin getAdminByEmailAndPassword(String email, String hashedPassword) throws SQLException {
        Connection conn = super.getConnection();

        if (conn != null) {
            String query = "SELECT * FROM Admin WHERE email = ? AND hash_password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return convertQueryResultToAdmin(rs);
            return null;
        }

        throw new SQLException("Connection to the database failed");
    }
}
