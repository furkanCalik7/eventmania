package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.entity.Organization;
import com.database.eventmania.backend.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping(path = "organization")
public class OrganizationController {
    private final OrganizationService organizationService;
    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping(path = "getOrganization/{organizationId}")
    public Organization getOrganizationById(@PathVariable("organizationId") Long organizationId) throws SQLException {
        return organizationService.getOrganizationById(organizationId);
    }
}
