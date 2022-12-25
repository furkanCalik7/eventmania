package com.database.eventmania.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Account {
    private Long accountId;
    private String email;
    private String hashPassword;

    public Account(Long accountId, String email, String hashPassword) {
        this.accountId = accountId;
        this.email = email;
        this.hashPassword = hashPassword;
    }
}
