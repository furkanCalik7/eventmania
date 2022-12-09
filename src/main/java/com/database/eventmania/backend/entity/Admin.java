package com.database.eventmania.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin extends Account {
    public Admin(Long adminId, String email, String hashPassword) {
        super(adminId, email, hashPassword);
    }
}
