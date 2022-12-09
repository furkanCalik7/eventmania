package com.database.eventmania.backend.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UnticketedEvent extends Event {
    private Long userId;
    private Integer capacity;

    public UnticketedEvent(Long eventId, Long adminId, String feedback, LocalDateTime verificationDate,
                           VerificationStatus verificationStatus, String eventName, String eventDescription,
                           LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                           Integer minimumAge, EventState currentState, Long userId, Integer capacity) {
        super(eventId, adminId, feedback, verificationDate, verificationStatus, eventName, eventDescription, startDate,
                endDate, isOnline, imageUrl, minimumAge, currentState);
        this.userId = userId;
        this.capacity = capacity;
    }

}
