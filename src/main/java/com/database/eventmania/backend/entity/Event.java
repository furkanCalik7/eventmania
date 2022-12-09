package com.database.eventmania.backend.entity;

import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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

    public Event(Long eventId, Long adminId, String feedback, LocalDateTime verificationDate,
            VerificationStatus verificationStatus, String eventName, String eventDescription,
            LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl, Integer minimumAge,
            EventState currentState) {
        this.eventId = eventId;
        this.adminId = adminId;
        this.feedback = feedback;
        this.verificationDate = verificationDate;
        this.verificationStatus = verificationStatus;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isOnline = isOnline;
        this.imageUrl = imageUrl;
        this.minimumAge = minimumAge;
        this.currentState = currentState;
    }

}

