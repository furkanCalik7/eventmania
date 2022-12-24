package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.DTO.EventDTO;
import com.database.eventmania.backend.entity.Location;
import com.database.eventmania.backend.entity.TicketedEvent;
import com.database.eventmania.backend.entity.UnticketedEvent;
import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.SalesChannel;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import com.database.eventmania.backend.model.EventModel;
import com.database.eventmania.backend.model.FilterModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;
import java.util.ArrayList;

@Repository
public class EventRepository extends BaseRepository {
    private Savepoint savepoint;

    public EventRepository() {
        super.connect();
    }

    public void createEventsInEventType(Long eventId, ArrayList<EventType> eventTypes) throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null)
            throw new SQLException("Connection to the database could not be established");
        for (EventType eventType : eventTypes) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO event_type (event_id, type_of_event) VALUES (?, ?)");
            stmt.setLong(1, eventId);
            stmt.setString(2, eventType.toString());
            if (stmt.executeUpdate() == 0) {
                conn.rollback(this.savepoint);
            }
        }
    }

    public Long createEvent(boolean isTicketed,
                            VerificationStatus verificationStatus, String eventName, String eventDescription,
                            LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                            Integer minimumAge, EventState currentState, ArrayList<EventType> eventTypes, SalesChannel salesChannel,
                            LocalDateTime saleStartTime, LocalDateTime saleEndTime,
                            Integer capacity, String locationName, Float latitude, Float longitude,
                            String postalCode, String state, String city, String country,
                            String addressDescription, Long eventCreatorId) throws SQLException {
        Connection conn = super.getConnection();
        conn.setAutoCommit(false);
        this.savepoint = conn.setSavepoint();
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
            conn.rollback(this.savepoint);
        }

        // query a sequence to get the last inserted event id
        String query = "SELECT currval('event_event_id_seq')";
        stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        long eventId = rs.getLong(1);

        // create event in event_type table
        createEventsInEventType(eventId, eventTypes);

        // create event in ticketed_event table
        if (isTicketed) {
            insertQuery = "INSERT INTO TicketedEvent (event_id, sales_channel, sale_start_time, sale_end_time, organization_id) " +
                    "VALUES (?, ?, ?, ?,?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setLong(1, eventId);
            stmt.setString(2, salesChannel.toString());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(saleStartTime));
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(saleEndTime));
            stmt.setLong(5, eventCreatorId);
        } else {
            insertQuery = "INSERT INTO UnticketedEvent (event_id, user_id, capacity) VALUES (?,?,?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setLong(1, eventId);
            stmt.setLong(2, eventCreatorId);
            stmt.setInt(3, capacity);

        }
        if (stmt.executeUpdate() == 0) {
            conn.rollback(this.savepoint);
        }
        // creates event in Location table
        if (!isOnline) {
            insertQuery = "INSERT INTO Location (event_id, location_name, latitude, longitude," +
                    "postal_code, state, city, country, address_description) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                conn.rollback(this.savepoint);
            }
        }
        conn.commit();
        return eventId;
    }


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
                        eventRs.getTimestamp("sale_end_time").toLocalDateTime(),
                        eventRs.getLong("organization_id")
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

    public ArrayList<EventModel> getAllEvents() throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null)
            throw new SQLException("Connection to the database failed");
        String query = "SELECT E.event_id, E.start_date, E.event_name, E.image_url,  E.is_online, L.location_name " +
                "FROM Event E " +
                "LEFT OUTER JOIN Location L ON E.event_id = L.event_id ";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        ArrayList<EventModel> events = new ArrayList<>();
        while (rs.next()) {
            EventModel event = new EventModel();
            FormatStyle dateStyle = FormatStyle.MEDIUM;
            FormatStyle timeStyle = FormatStyle.SHORT;
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle);
            event.setEventId(rs.getLong("event_id"));
            event.setStartdate(rs.getTimestamp("start_date").toLocalDateTime().format(formatter));
            event.setTitle(rs.getString("event_name"));
            event.setImageUrl(rs.getString("image_url"));
            if (rs.getBoolean("is_online"))
                event.setLocationName("Online");
            else
                event.setLocationName(rs.getString("location_name"));
            events.add(event);
        }
        return events;
    }

    public ArrayList<EventModel> getFilteredEvents(FilterModel filterModel) throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null) {
            throw new SQLException("Connection to the database failed");
        }
        String query = "SELECT DISTINCT E.event_id, E.start_date, E.event_name, E.image_url,  E.is_online, L.location_name " +
        "FROM Event E LEFT OUTER JOIN event_type ET ON E.event_id = ET.event_id " +
        "LEFT OUTER JOIN Location L ON E.event_id = L.event_id " +
        "WHERE " +
                "(? is null OR UPPER(E.event_name) LIKE UPPER(?) OR " +
                "? is null OR UPPER(L.country) LIKE UPPER(?) OR " +
                "? is null OR UPPER(L.city) LIKE UPPER(?) OR " +
                "? is null OR UPPER(L.state) LIKE UPPER(?) OR " +
                "? is null OR UPPER(L.postal_code) LIKE UPPER(?) OR " +
                "? is null OR UPPER(L.location_name) LIKE UPPER(?) )  AND "+
                "?::timestamp is null OR E.start_date >= ? AND " +
                "?::timestamp is null OR E.end_date <= ? AND " +
                "(?::varchar[] is null OR ET.type_of_event IN (NULLIF(?, ET.type_of_event)))";

        PreparedStatement stmt = conn.prepareStatement(query);
        for(int x = 1; x < 13; x++){
            stmt.setString(x, filterModel.getName());
        }


        stmt.setTimestamp(13, (!filterModel.getStartDate().equals("") ? Timestamp.valueOf(filterModel.getStartDate()) : null ));
        stmt.setTimestamp(14, (!filterModel.getStartDate().equals("") ? Timestamp.valueOf(filterModel.getStartDate()) : null ));
        stmt.setTimestamp(15, (!filterModel.getStartDate().equals("") ? Timestamp.valueOf(filterModel.getEndDate()) : null ));
        stmt.setTimestamp(16, (!filterModel.getStartDate().equals("") ? Timestamp.valueOf(filterModel.getEndDate()) : null ));
        stmt.setArray(17, (filterModel.getEventTypes() != null ? conn.createArrayOf("varchar", filterModel.getEventTypes().toArray()) : null));
        stmt.setArray(18, (filterModel.getEventTypes() != null ? conn.createArrayOf("varchar", filterModel.getEventTypes().toArray()) : null));

        ResultSet rs = stmt.executeQuery();
        ArrayList<EventModel> events = new ArrayList<>();
        while (rs.next()) {
            EventModel event = new EventModel();
            FormatStyle dateStyle = FormatStyle.MEDIUM;
            FormatStyle timeStyle = FormatStyle.SHORT;
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle);
            event.setEventId(rs.getLong("event_id"));
            event.setStartdate(rs.getTimestamp("start_date").toLocalDateTime().format(formatter));
            event.setTitle(rs.getString("event_name"));
            event.setImageUrl(rs.getString("image_url"));
            if (rs.getBoolean("is_online"))
                event.setLocationName("ONLINE");
            else
                event.setLocationName(rs.getString("location_name"));
            events.add(event);

        }
        return events;
    }
}
