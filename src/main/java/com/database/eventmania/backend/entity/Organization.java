package com.database.eventmania.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Organization {
    private Long organizationId;
    private String email;
    private String hashPassword;
    private String organizationName;
    private String description;
    private String phoneNumber;

    public Organization(Long organizationId, String email, String hashPassword,
                        String organizationName, String description, String phoneNumber) {
        this.organizationId = organizationId;
        this.email = email;
        this.hashPassword = hashPassword;
        this.organizationName = organizationName;
        this.description = description;
        this.phoneNumber = phoneNumber;
    }
}
