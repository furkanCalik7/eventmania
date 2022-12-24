package com.database.eventmania.backend.entity;

import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class Event {
    private Long eventId;
    private Long adminId;
    private String feedback;
    private LocalDateTime verificationDate;
    private VerificationStatus verificationStatus;
    private String eventName;
    private String eventDescription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isOnline;
    private String imageUrl;
    private Integer minimumAge;
    private EventState currentState;
    private ArrayList<EventType> eventTypes;
    private boolean isTicketed;

}

