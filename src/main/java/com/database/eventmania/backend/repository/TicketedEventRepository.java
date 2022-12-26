package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.SalesChannel;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public Long createTicketedEvent(
            VerificationStatus verificationStatus, String eventName, String eventDescription,
            LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
            Integer minimumAge, EventState currentState, ArrayList<EventType> eventTypes, SalesChannel salesChannel,
            LocalDateTime saleStartTime, LocalDateTime saleEndTime, String locationName, Float latitude, Float longitude,
            String postalCode, String state, String city, String country,
            String addressDescription, String email) throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null)
            throw new SQLException("Connection to the database could not be established");
        //get organization id from the table account_with_type by email
        String query = "SELECT organization_id FROM account_with_type WHERE email = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
        rs.next();
        Long organizationId = rs.getLong("organization_id");
        return eventRepository.createEvent(true, verificationStatus, eventName,
                eventDescription, startDate, endDate, isOnline, imageUrl, minimumAge, EventState.UNPUBLISHED, eventTypes, salesChannel,
                saleStartTime, saleEndTime, null, locationName, latitude, longitude, postalCode, state, city,
                country, addressDescription, organizationId);
    }
}
