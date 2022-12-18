package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import com.database.eventmania.backend.repository.UnticketedEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class UnticketedEventService {
    private final UnticketedEventRepository unticketedEventRepository;

    @Autowired
    public UnticketedEventService(UnticketedEventRepository unticketedEventRepository) {
        this.unticketedEventRepository = unticketedEventRepository;
    }

    public boolean createUnticketedEvent(Long adminId, String feedback, LocalDateTime verificationDate,
                                         VerificationStatus verificationStatus, String eventName, String eventDescription,
                                         LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                                         Integer minimumAge, EventState currentState, EventType eventType,
                                         Long userId, Integer capacity, String locationName, Float latitude, Float longitude,
                                         String postalCode, String state, String city, String street, String country,
                                         String addressDescription) throws SQLException {
        return unticketedEventRepository.createUnticketedEvent(adminId, feedback, verificationDate, verificationStatus, eventName,
                eventDescription, startDate, endDate, isOnline, imageUrl, minimumAge, currentState, eventType, userId, capacity,
                locationName, latitude, longitude, postalCode, state, city, street, country, addressDescription);
    }
}
