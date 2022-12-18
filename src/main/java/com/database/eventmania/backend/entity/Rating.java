package com.database.eventmania.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Rating {
    private Long ratingId;
    private Long eventId;
    private Long userId;
    private Integer point;
    private String topic;
    private String comment;
}
