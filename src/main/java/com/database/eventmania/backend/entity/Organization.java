package com.database.eventmania.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Organization extends Account {
    private String organizationName;
    private String description;
    private String phoneNumber;

    public Organization(Long organizationId, String email, String hashPassword,
                        String organizationName, String description, String phoneNumber) {
        super(organizationId, email, hashPassword);
        this.organizationName = organizationName;
        this.description = description;
        this.phoneNumber = phoneNumber;
    }
}
