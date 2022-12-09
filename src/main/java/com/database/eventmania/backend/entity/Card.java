package com.database.eventmania.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Card {
    private String cardNumber;
    private String cardOwner;
    private String cardTitle;
    private Integer expirationMonth;
    private Integer expirationYear;
}
