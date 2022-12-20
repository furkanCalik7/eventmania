package com.database.eventmania.backend.entity;

import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.SalesChannel;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TicketedEvent extends Event {
    private SalesChannel salesChannel;
    private Long organizationId;
    private LocalDateTime saleStartTime;
    private LocalDateTime saleEndTime;

    @Transient
    private Integer capacity;

    public TicketedEvent(Long eventId, Long adminId, String feedback, LocalDateTime verificationDate,
                         VerificationStatus verificationStatus, String eventName, String eventDescription,
                         LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                         Integer minimumAge, EventState currentState, EventType eventType, SalesChannel salesChannel,
                         LocalDateTime saleStartTime, LocalDateTime saleEndTime, Long organizationId) {
        super(eventId, adminId, feedback, verificationDate, verificationStatus, eventName, eventDescription, startDate,
                endDate, isOnline, imageUrl, minimumAge, currentState, eventType, true);
        this.salesChannel = salesChannel;
        this.saleStartTime = saleStartTime;
        this.saleEndTime = saleEndTime;
        this.organizationId = organizationId;
    }
    // TODO: getCapacity() method should be implemented
}

