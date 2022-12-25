package com.database.eventmania.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {
    private String name;
    private String desc;
    private String capacity;
    private String price;
}
