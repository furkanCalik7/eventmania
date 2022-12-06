package com.database.eventmania.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Account {
    private Long accountId;
    private String email;
    private String password;

    public Account(Long accountId, String email, String password) {
        this.accountId = accountId;
        this.email = email;
        this.password = password;
    }
}