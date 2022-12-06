package com.database.eventmania.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Account {

    private Long accountId;
    private String email;
    private String password;

}