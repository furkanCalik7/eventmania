package com.database.eventmania.backend.entity;

import java.time.LocalDate;

public class Event {
    private Long eventId;
    private Long adminId;
    private String feedback;
    private LocalDate verificationDate;
    private VerificationStatus verificationStatus;
    private String eventName;
    private String eventDescription;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isOnline;
    private String imageUrl;
    private Integer minimumAge;
    private EventState currentState;

    public Event(Long eventId, Long adminId, String feedback, LocalDate verificationDate,
            VerificationStatus verificationStatus, String eventName, String eventDescription,
            LocalDate startDate, LocalDate endDate, Boolean isOnline, String imageUrl, Integer minimumAge,
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

