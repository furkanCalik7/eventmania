package com.database.eventmania.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Category {
    private Long eventId;
    private String categoryName;
    private String categoryDescription;
    private Integer capacity;
    private Integer price;
}
