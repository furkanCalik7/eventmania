package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.entity.Admin;
import com.database.eventmania.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping(path = "admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(path = "getAdmin/{adminId}")
    public Admin getAdminById(@PathVariable("adminId") Long adminId) throws SQLException {
        return adminService.getAdminById(adminId);
    }
}
