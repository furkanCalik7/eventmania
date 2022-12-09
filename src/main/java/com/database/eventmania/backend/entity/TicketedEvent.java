package com.database.eventmania.backend.entity;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TicketedEvent extends Event {
    private SalesChannel salesChannel;
    private LocalDateTime saleEndTime;

    @Transient
    private Integer capacity;

    public TicketedEvent(Long eventId, Long adminId, String feedback, LocalDate verificationDate,
                         VerificationStatus verificationStatus, String eventName, String eventDescription,
                         LocalDate startDate, LocalDate endDate, Boolean isOnline, String imageUrl,
                         Integer minimumAge, EventState currentState, SalesChannel salesChannel, LocalDateTime saleEndTime) {
        super(eventId, adminId, feedback, verificationDate, verificationStatus, eventName, eventDescription, startDate,
                endDate, isOnline, imageUrl, minimumAge, currentState);
        this.salesChannel = salesChannel;
        this.saleEndTime = saleEndTime;
    }

    // TODO: getCapacity() method should be implemented

}

