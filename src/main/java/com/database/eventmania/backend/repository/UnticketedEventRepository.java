package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public class UnticketedEventRepository extends BaseRepository {
    private final EventRepository eventRepository;

    @Autowired
    public UnticketedEventRepository(EventRepository eventRepository) {
        super.connect();
        this.eventRepository = eventRepository;
    }

    public Long createUnticketedEvent(VerificationStatus verificationStatus, String eventName, String eventDescription,
                                      LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                                      Integer minimumAge, EventState currentState, ArrayList<EventType> eventTypes,
                                      Integer capacity, String locationName, Float latitude, Float longitude,
                                      String postalCode, String state, String city, String country,
                                      String addressDescription, String email) throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null)
            throw new SQLException("Connection to the database could not be established");

        String query = "SELECT organization_id FROM account_with_type WHERE email = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
        rs.next();
        Long userId = rs.getLong("organization_id");

        if (conn == null)
            throw new SQLException("Connection to the database could not be established");
        return eventRepository.createEvent(false, verificationStatus, eventName,
                eventDescription, startDate, endDate, isOnline, imageUrl, minimumAge, currentState, eventTypes, null,
                null, null, capacity, locationName, latitude, longitude, postalCode, state, city,
                country, addressDescription, userId);
    }
}
