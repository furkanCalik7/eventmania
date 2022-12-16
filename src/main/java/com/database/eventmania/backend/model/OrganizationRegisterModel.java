package com.database.eventmania.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrganizationRegisterModel {
    private String email;
    private String password;
    private String organizationName;
    private String description;
    private String phoneNumber;

}
