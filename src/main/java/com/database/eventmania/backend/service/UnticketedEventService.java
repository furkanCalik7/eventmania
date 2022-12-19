package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import com.database.eventmania.backend.repository.UnticketedEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
                                         Integer minimumAge, EventState currentState, ArrayList<EventType> eventTypes,
                                         Long userId, Integer capacity, String locationName, Float latitude, Float longitude,
                                         String postalCode, String state, String city, String country,
                                         String addressDescription) throws SQLException {
        return unticketedEventRepository.createUnticketedEvent(verificationStatus, eventName,
                eventDescription, startDate, endDate, isOnline, imageUrl, minimumAge, currentState, eventTypes, userId, capacity,
                locationName, latitude, longitude, postalCode, state, city, country, addressDescription);
    }
}
