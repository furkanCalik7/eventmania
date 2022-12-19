package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.SalesChannel;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public class TicketedEventRepository extends BaseRepository {
    private final EventRepository eventRepository;

    public TicketedEventRepository(EventRepository eventRepository) {
        super.connect();
        this.eventRepository = eventRepository;
    }

    public boolean createTicketedEvent(
            VerificationStatus verificationStatus, String eventName, String eventDescription,
            LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
            Integer minimumAge, EventState currentState, ArrayList<EventType> eventTypes, SalesChannel salesChannel,
            LocalDateTime saleStartTime, LocalDateTime saleEndTime, String locationName, Float latitude, Float longitude,
            String postalCode, String state, String city, String country,
            String addressDescription) throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null)
            throw new SQLException("Connection to the database could not be established");

        return eventRepository.createEvent(true, verificationStatus, eventName,
                eventDescription, startDate, endDate, isOnline, imageUrl, minimumAge, currentState, eventTypes, salesChannel,
                saleStartTime, saleEndTime, null, null, locationName, latitude, longitude, postalCode, state, city,
                country, addressDescription);
    }
}
