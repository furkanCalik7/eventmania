package com.database.eventmania.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Location {
    private Long eventId;
    private Float latitude;
    private Float longitude;
    private String locationName;
    private String addressDescription;
    private String country;
    private String postalCode;
    private String state;
    private String city;
    private String street;


}
