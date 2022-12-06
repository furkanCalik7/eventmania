package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.Admin;
import com.database.eventmania.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

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
}
