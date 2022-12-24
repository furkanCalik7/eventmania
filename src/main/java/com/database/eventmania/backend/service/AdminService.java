package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.Admin;
import com.database.eventmania.backend.entity.Organization;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import com.database.eventmania.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin getAdminById(Long adminId) throws SQLException {
        return adminRepository.getAdminById(adminId);
    }

    public boolean updateOrganizationApprovalStatus(String organizationEmail, String adminEmail, String feedback, LocalDateTime verificationDate, VerificationStatus verificationStatus) throws SQLException {
        return adminRepository.updateOrganizationApprovalStatus(organizationEmail, adminEmail, feedback, verificationDate, verificationStatus);
    }

    public ArrayList<Organization> listUnapprovedOrganizations() throws SQLException {
        return adminRepository.listUnapprovedOrganizations();
    }
}
