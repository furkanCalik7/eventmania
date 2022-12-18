package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.SalesChannel;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import com.database.eventmania.backend.repository.TicketedEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class TicketedEventService {
    private final TicketedEventRepository ticketedEventRepository;
    @Autowired
    public TicketedEventService(TicketedEventRepository ticketedEventRepository) {
        this.ticketedEventRepository = ticketedEventRepository;
    }

    public boolean createTicketedEvent(Long adminId, String feedback, LocalDateTime verificationDate,
                                       VerificationStatus verificationStatus, String eventName, String eventDescription,
                                       LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                                       Integer minimumAge, EventState currentState, EventType eventType, SalesChannel salesChannel,
                                       LocalDateTime saleStartTime, LocalDateTime saleEndTime,
                                       String locationName, Float latitude, Float longitude,
                                       String postalCode, String state, String city, String street, String country,
                                       String addressDescription) throws SQLException {
        return ticketedEventRepository.createTicketedEvent(adminId, feedback, verificationDate, verificationStatus, eventName,
                eventDescription, startDate, endDate, isOnline, imageUrl, minimumAge, currentState, eventType, salesChannel,
                saleStartTime, saleEndTime, locationName, latitude, longitude, postalCode, state, city,
                street, country, addressDescription);
    }
}
