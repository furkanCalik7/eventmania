package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Repository
public class UnticketedEventRepository extends BaseRepository {
    private final EventRepository eventRepository;
    @Autowired
    public UnticketedEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    public boolean createUnticketedEvent(Long adminId, String feedback, LocalDateTime verificationDate,
                                         VerificationStatus verificationStatus, String eventName, String eventDescription,
                                         LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                                         Integer minimumAge, EventState currentState, EventType eventType,
                                         Long userId, Integer capacity, String locationName, Float latitude, Float longitude,
                                         String postalCode, String state, String city, String street, String country,
                                         String addressDescription) throws SQLException {
        Connection conn = super.getConnection();

        if (conn == null)
            throw new SQLException("Connection to the database could not be established");

        return eventRepository.createEvent(false, adminId, feedback, verificationDate, verificationStatus, eventName,
                eventDescription, startDate, endDate, isOnline, imageUrl, minimumAge, currentState, eventType, null,
                null, null, userId, capacity, locationName, latitude, longitude, postalCode, state, city,
                street, country, addressDescription);
    }
}
