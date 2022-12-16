package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.Organization;
import com.database.eventmania.backend.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public Organization getOrganizationById(Long organizationId) throws SQLException {
        return organizationRepository.getOrganizationById(organizationId);
    }

    public Organization getOrganizationByEmailAndPassword(String email, String password) throws SQLException {
        return organizationRepository.getOrganizationByEmailAndPassword(email, password);
    }

    public boolean saveOrganization(String email, String password,
                                    String organizationName, String description, String phoneNumber) throws SQLException {
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        return organizationRepository.saveOrganization(email, hashedPassword, organizationName, description, phoneNumber);
    }
}
