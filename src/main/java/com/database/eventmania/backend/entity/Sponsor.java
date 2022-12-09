package com.database.eventmania.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Sponsor {
    private Long sponsorId;
    private String sponsorName;
    private String details;
    private String imageUrl;
}
