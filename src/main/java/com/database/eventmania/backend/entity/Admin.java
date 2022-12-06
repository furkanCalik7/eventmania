package com.database.eventmania.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin extends Account {
    public Admin(Long accountId, String email, String password) {
        super(accountId, email, password);
    }
}
