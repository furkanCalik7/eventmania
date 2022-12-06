package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.Organization;
import com.database.eventmania.backend.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization getOrganizationById(Long organizationId) throws SQLException {
        return organizationRepository.getOrganizationById(organizationId);
    }
}
