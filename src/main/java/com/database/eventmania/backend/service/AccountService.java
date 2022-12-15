package com.database.eventmania.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.database.eventmania.backend.entity.Admin;
import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.entity.Organization;
import com.database.eventmania.backend.repository.AdminRepository;
import com.database.eventmania.backend.repository.OrganizationRepository;
import com.database.eventmania.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final OrganizationRepository organizationRepository;

    @Autowired
    public AccountService(UserRepository userRepository, AdminRepository adminRepository,
                          OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.organizationRepository = organizationRepository;
    }
}
