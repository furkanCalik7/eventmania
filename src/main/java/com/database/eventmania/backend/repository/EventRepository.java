package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.DTO.EventDTO;
import com.database.eventmania.backend.entity.Location;
import com.database.eventmania.backend.entity.TicketedEvent;
import com.database.eventmania.backend.entity.UnticketedEvent;
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
import java.util.Objects;
import java.util.ArrayList;

@Repository
public class EventRepository extends BaseRepository {

    public EventRepository() {
        super.connect();
    }

    public boolean createEventsInEventType(Long eventId, ArrayList<EventType> eventTypes) throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null)
            throw new SQLException("Connection to the database could not be established");
        for (EventType eventType : eventTypes) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO event_type (event_id, type_of_event) VALUES (?, ?)");
            stmt.setLong(1, eventId);
            stmt.setString(2, eventType.toString());
            stmt.executeUpdate();
            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Creating event in event_type table failed, no rows affected.");
            }
        }
        return true;
    }

    public boolean createEvent(boolean isTicketed,
                               VerificationStatus verificationStatus, String eventName, String eventDescription,
                               LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                               Integer minimumAge, EventState currentState, ArrayList<EventType> eventTypes, SalesChannel salesChannel,
                               LocalDateTime saleStartTime, LocalDateTime saleEndTime, Long userId,
                               Integer capacity, String locationName, Float latitude, Float longitude,
                               String postalCode, String state, String city, String country,
                               String addressDescription) throws SQLException {
        Connection conn = super.getConnection();

        String insertQuery = "INSERT INTO Event (" +
                "event_name, description, start_date, end_date, is_online, image_url, minimum_age, " +
                "current_state, verification_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // I removed the not null constraint in the database for admin_id since it can be null
        PreparedStatement stmt = conn.prepareStatement(insertQuery);
        stmt.setString(1, eventName);
        stmt.setString(2, eventDescription);
        stmt.setTimestamp(3, java.sql.Timestamp.valueOf(startDate));
        stmt.setTimestamp(4, java.sql.Timestamp.valueOf(endDate));
        stmt.setBoolean(5, isOnline);
        stmt.setString(6, imageUrl);
        stmt.setInt(7, minimumAge);
        stmt.setString(8, currentState.toString());
        // The newly created event is always in the upcoming state when it is created (I think)
        stmt.setString(9, verificationStatus.toString());

        if (stmt.executeUpdate() == 0) {
            throw new SQLException("Creating event failed, no rows affected.");
        }

        // query a sequence to get the last inserted event id
        String query = "SELECT currval('event_event_id_seq')";
        stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        Long eventId = rs.getLong(1);

        // create event in event_type table
        createEventsInEventType(eventId, eventTypes);

        // create event in ticketed_event table
        if (isTicketed) {
            insertQuery = "INSERT INTO TicketedEvent (event_id, sales_channel, sale_start_time, sale_end_time) " +
                    "VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setLong(1, eventId);
            stmt.setString(2, salesChannel.toString());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(saleStartTime));
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(saleEndTime));

            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Creating event in ticketed_event table failed, no rows affected.");
            }
        } else {
            insertQuery = "INSERT INTO UnticketedEvent (event_id, user_id, capacity) VALUES (?,?,?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setLong(1, eventId);
            stmt.setLong(2, userId);
            stmt.setInt(3, capacity);

            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Creating event in unticketed_event table failed, no rows affected.");
            }
        }
        // TODO: buradaki location_name e bir ayar yapmamiz lazim
        // create event in Location table
        insertQuery = "INSERT INTO Location (event_id, location_name, latitude, longitude," +
                "postal_code, state, city, country, address_description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        stmt = conn.prepareStatement(insertQuery);
        stmt.setLong(1, eventId);
        stmt.setString(2, locationName);
        stmt.setDouble(3, latitude);
        stmt.setDouble(4, longitude);
        stmt.setString(5, postalCode);
        stmt.setString(6, state);
        stmt.setString(7, city);
        stmt.setString(8, country);
        stmt.setString(9, addressDescription);

        if (stmt.executeUpdate() == 0) {
            throw new SQLException("Creating event in Location table failed, no rows affected.");
        }

        return true;
    }

    /*
    // TODO: Add a method to create an event in the correct event table
    public boolean createEvent(Long adminId, String feedback, LocalDateTime verificationDate,
                               VerificationStatus verificationStatus, String eventName, String eventDescription,
                               LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                               Integer minimumAge, EventState currentState, EventType eventType, boolean isTicketed) throws SQLException {
        Connection conn = super.getConnection();

        if (conn == null)
            return false;

        if (isTicketed) {

        }
        String query = "INSERT INTO Event (admin_id, feedback, verification_date, verification_status, event_name, " +
                "description, start_date, end_date, is_online, image_url, minimum_age, current_state) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setLong(1, adminId);
        stmt.setString(2, feedback);
        stmt.setTimestamp(3, java.sql.Timestamp.valueOf(verificationDate));
        stmt.setString(4, verificationStatus.toString());
        stmt.setString(5, eventName);
        stmt.setString(6, eventDescription);
        stmt.setTimestamp(7, java.sql.Timestamp.valueOf(startDate));
        stmt.setTimestamp(8, java.sql.Timestamp.valueOf(endDate));
        stmt.setBoolean(9, isOnline);
        stmt.setString(10, imageUrl);
        stmt.setInt(11, minimumAge);
        stmt.setString(12, currentState.toString());

        if (stmt.executeUpdate() == 0)
            throw new SQLException("Creating event failed, no rows affected.");

        // get the current value of the event_id sequence
        query = "SELECT currval('event_event_id_seq')";
        stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        Long eventId = rs.getLong(1);

        // then create the event in the event_type table
        createEventInEventType(eventId, eventType);

        return true;
    }

     */

    public boolean deleteEvent(Long eventId) throws SQLException {
        Connection conn = super.getConnection();

        if (conn == null)
            return false;

        String query = "DELETE FROM Event WHERE event_id = ?";

        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setLong(1, eventId);

        return stmt.executeUpdate() > 0;
    }


    public EventDTO getEventById(Long eventId) throws SQLException {
        Connection conn = super.getConnection();

        if (conn == null) {
            throw new SQLException("Connection to the database failed");
        }

        EventDTO dto = new EventDTO();

        String query = "SELECT * FROM event_with_type WHERE event_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setLong(1, eventId);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            if (Objects.equals(rs.getString("ticketed_type"), "ticketed")) {
                // find the event in the ticketed_event table with the given event_id
                query = "SELECT * " +
                        "FROM TicketedEvent JOIN event_with_type USING(event_id) " +
                        "WHERE event_id = ?";

                stmt = conn.prepareStatement(query);
                stmt.setLong(1, eventId);
                ResultSet eventRs = stmt.executeQuery();
                eventRs.next();

                TicketedEvent event = new TicketedEvent(
                        eventRs.getLong("event_id"),
                        eventRs.getLong("admin_id"),
                        eventRs.getString("feedback"),
                        eventRs.getTimestamp("verification_date").toLocalDateTime(),
                        VerificationStatus.valueOf(eventRs.getString("verification_status")),
                        eventRs.getString("event_name"),
                        eventRs.getString("description"),
                        eventRs.getTimestamp("start_date").toLocalDateTime(),
                        eventRs.getTimestamp("end_date").toLocalDateTime(),
                        eventRs.getBoolean("is_online"),
                        eventRs.getString("image_url"),
                        eventRs.getInt("minimum_age"),
                        EventState.valueOf(eventRs.getString("current_state")),
                        EventType.valueOf(eventRs.getString("type_of_event")),
                        SalesChannel.valueOf(eventRs.getString("sales_channel")),
                        eventRs.getTimestamp("sale_start_time").toLocalDateTime(),
                        eventRs.getTimestamp("sale_end_time").toLocalDateTime()
                );

                dto.addObject("event", event);

            } else if (Objects.equals(rs.getString("ticketed_type"), "unticketed")) {
                // find the event in the unticketed_event table with the given event_id
                query = "SELECT * " +
                        "FROM UnticketedEvent JOIN event_with_type USING(event_id) " +
                        "WHERE event_id = ?";

                stmt = conn.prepareStatement(query);
                stmt.setLong(1, eventId);
                ResultSet eventRs = stmt.executeQuery();
                eventRs.next();

                UnticketedEvent event = new UnticketedEvent(
                        eventRs.getLong("event_id"),
                        eventRs.getLong("admin_id"),
                        eventRs.getString("feedback"),
                        eventRs.getTimestamp("verification_date").toLocalDateTime(),
                        VerificationStatus.valueOf(eventRs.getString("verification_status")),
                        eventRs.getString("event_name"),
                        eventRs.getString("description"),
                        eventRs.getTimestamp("start_date").toLocalDateTime(),
                        eventRs.getTimestamp("end_date").toLocalDateTime(),
                        eventRs.getBoolean("is_online"),
                        eventRs.getString("image_url"),
                        eventRs.getInt("minimum_age"),
                        EventState.valueOf(eventRs.getString("current_state")),
                        EventType.valueOf(eventRs.getString("type_of_event")),
                        eventRs.getLong("user_id"),
                        eventRs.getInt("capacity")
                );

                dto.addObject("event", event);
            }

            Location location = new Location(
                    rs.getLong("event_id"),
                    rs.getFloat("latitude"),
                    rs.getFloat("longitude"),
                    rs.getString("location_name"),
                    rs.getString("address_description"),
                    rs.getString("country"),
                    rs.getString("postal_code"),
                    rs.getString("state"),
                    rs.getString("city"),
                    rs.getString("street"));

            // event, location, ticketed_event, unticketed_event
            dto.addObject("location", location);
            dto.addObject("ticketed_type", rs.getString("ticketed_type"));

            return dto;
        }

        throw new SQLException("Event with id " + eventId + " does not exist");
    }

    public ArrayList<EventDTO> getAllEvents() throws SQLException {
        Connection conn = super.getConnection();
        // create a query that gets all the event ids

        if (conn == null)
            throw new SQLException("Connection to the database failed");

        String query = "SELECT event_id FROM event_with_type";
        ArrayList<EventDTO> events = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            events.add(getEventById(rs.getLong("event_id")));
        }

        return events;
    }
}
