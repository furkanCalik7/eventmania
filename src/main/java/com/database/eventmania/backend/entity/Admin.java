package com.database.eventmania.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin {
    private Long adminId;
    private String email;
    private String hashPassword;
    public Admin(Long adminId, String email, String hashPassword) {
        this.adminId = adminId;
        this.email = email;
        this.hashPassword = hashPassword;
    }
}
