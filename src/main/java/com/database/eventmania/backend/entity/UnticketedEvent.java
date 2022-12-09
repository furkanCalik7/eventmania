package com.database.eventmania.backend.entity;

import java.time.LocalDate;
public class UnticketedEvent extends Event {
    private Long eventId;
    private Long userId;
    private Integer capacity;

    public UnticketedEvent(Long event_id, Long adminId, String feedback, LocalDate verificationDate,
                           VerificationStatus verificationStatus, String eventName, String eventDescription,
                           LocalDate startDate, LocalDate endDate, Boolean isOnline, String imageUrl,
                           Integer minimumAge, EventState currentState, Long eventId, Long userId, Integer capacity) {
        super(eventId, adminId, feedback, verificationDate, verificationStatus, eventName, eventDescription, startDate,
                endDate, isOnline, imageUrl, minimumAge, currentState);



    }

}
