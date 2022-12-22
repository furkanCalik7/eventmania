package com.database.eventmania.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterModel {
    private ArrayList<String> eventTypes;
    private String ticketType;
    private String startDate;
    private String endDate;
    private String name;
    private String latitude;
    private String longitude;
}
