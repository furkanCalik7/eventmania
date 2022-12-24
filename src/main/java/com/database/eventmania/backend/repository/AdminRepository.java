package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.Admin;
import com.database.eventmania.backend.entity.Organization;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

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

    public boolean updateOrganizationApprovalStatus(String organizationEmail, String adminEmail, String feedback, LocalDateTime verificationDate, VerificationStatus verificationStatus) throws SQLException {
        Connection conn = super.getConnection();

        if (conn == null)
            throw new SQLException("Connection to the database could not be established");

        String query = "SELECT * FROM Organization WHERE email = ?";
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setString(1, organizationEmail);

        if (stmt.executeUpdate() == 1)
            throw new SQLException("Organization with the given email does not exist");

        query = "UPDATE Organization SET verification_status = ?, feedback = ?, verification_date = ? WHERE email = ?";
        stmt = conn.prepareStatement(query);

        stmt.setString(1, verificationStatus.toString());
        stmt.setString(2, feedback);
        stmt.setTimestamp(3, Timestamp.valueOf(verificationDate));
        stmt.setString(4, organizationEmail);

        int result = stmt.executeUpdate();
        if (result == 1)
            return true;
        throw new SQLException("Organization could not be approved");
    }

    public ArrayList<Organization> listUnapprovedOrganizations() throws SQLException {
        Connection conn = super.getConnection();

        if (conn == null)
            throw new SQLException("Connection to the database could not be established");

        String query = "SELECT * FROM Organization WHERE verification_status != ?";
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setString(1, VerificationStatus.CONFIRMED.toString());

        ResultSet rs = stmt.executeQuery();

        ArrayList<Organization> organizations = new ArrayList<>();
        while (rs.next())
            organizations.add(convertQueryResultToOrganization(rs));

        return organizations;
    }
}
