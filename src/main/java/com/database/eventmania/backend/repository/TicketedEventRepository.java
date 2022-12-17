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

@Repository
public class TicketedEventRepository extends BaseRepository {
    private final EventRepository eventRepository;

    public TicketedEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public boolean createTicketedEvent(Long adminId, String feedback, LocalDateTime verificationDate,
                                       VerificationStatus verificationStatus, String eventName, String eventDescription,
                                       LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                                       Integer minimumAge, EventState currentState, EventType eventType, SalesChannel salesChannel,
                                       LocalDateTime saleStartTime, LocalDateTime saleEndTime) throws SQLException {
        Connection conn = super.getConnection();

        if (conn == null)
            return false;

        eventRepository.createEvent(adminId, feedback, verificationDate, verificationStatus,
                                    eventName, eventDescription, startDate, endDate, isOnline, imageUrl,
                                    minimumAge, currentState, eventType);

        // get the current value of the event_id sequence
        String query = "SELECT currval('event_event_id_seq')";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        Long eventId = rs.getLong(1);

        // then create the ticketed event in the ticketed_event table
        query = "INSERT INTO TicketedEvent (event_id, sales_channel, sale_start_time, sale_end_time) " +
                "VALUES (?, ?, ?, ?)";

        stmt = conn.prepareStatement(query);
        stmt.setLong(1, eventId);
        stmt.setString(2, salesChannel.toString());
        stmt.setTimestamp(3, java.sql.Timestamp.valueOf(saleStartTime));
        stmt.setTimestamp(4, java.sql.Timestamp.valueOf(saleEndTime));

        return stmt.executeUpdate() > 0;
    }
}
