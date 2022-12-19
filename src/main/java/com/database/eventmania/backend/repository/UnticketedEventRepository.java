package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public class UnticketedEventRepository extends BaseRepository {
    private final EventRepository eventRepository;
    @Autowired
    public UnticketedEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    public boolean createUnticketedEvent(VerificationStatus verificationStatus, String eventName, String eventDescription,
                                         LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                                         Integer minimumAge, EventState currentState, ArrayList<EventType> eventTypes,
                                         Long userId, Integer capacity, String locationName, Float latitude, Float longitude,
                                         String postalCode, String state, String city, String country,
                                         String addressDescription) throws SQLException {
        Connection conn = super.getConnection();

        if (conn == null)
            throw new SQLException("Connection to the database could not be established");

        return eventRepository.createEvent(false,  verificationStatus, eventName,
                eventDescription, startDate, endDate, isOnline, imageUrl, minimumAge, currentState, eventTypes, null,
                null, null, userId, capacity, locationName, latitude, longitude, postalCode, state, city,
                country, addressDescription);
    }
}
