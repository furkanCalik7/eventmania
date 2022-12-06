package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.Organization;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class OrganizationRepository extends BaseRepository {
    public OrganizationRepository() {
        super.connect();
    }

    public Organization getOrganizationById(Long organizationId) throws SQLException {
        Connection conn = super.getConnection();
        if (conn != null) {
            String query = "SELECT * FROM Organization WHERE organization_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setLong(1, organizationId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return convertQueryResultToOrganization(rs);
            }
        }
        return null;
    }
}
