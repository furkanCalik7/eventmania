package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.SalesChannel;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import com.database.eventmania.backend.model.EventModel;
import com.database.eventmania.backend.model.FilterModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Repository
public class EventRepository extends BaseRepository {
    private Savepoint savepoint;

    public EventRepository() {
        super.connect();
    }


    public boolean publishTicketedEvent(Long eventId) throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null)
            throw new SQLException("Connection to the database could not be established");
        String query = "UPDATE event SET current_state = ? WHERE event_id = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, EventState.UPCOMING.toString());
        statement.setLong(2, eventId);
        statement.executeUpdate();
        return true;
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
        stmt.setString(8, EventState.UNPUBLISHED.toString());
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


    public EventModel getEventById(Long eventId) throws SQLException {
        Connection conn = super.getConnection();

        if (conn == null) {
            throw new SQLException("Connection to the database failed");
        }

        String query = "SELECT * FROM event_with_type WHERE event_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setLong(1, eventId);

        ResultSet rs = stmt.executeQuery();

        boolean first = true;
        EventModel event = new EventModel();

        while (rs.next()) {
            if (!first) {
                event.getEventTypes().add(rs.getString("type_of_event"));
                continue;
            }

            LocalDateTime verificationDate = rs.getTimestamp("verification_date") != null
                    ? rs.getTimestamp("verification_date").toLocalDateTime() : null;


            if (Objects.equals(rs.getString("ticketed_type").toLowerCase(), "ticketed")) {
                query = "SELECT * FROM ticketedevent WHERE event_id = ?";
                stmt = conn.prepareStatement(query);
                stmt.setLong(1, eventId);
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();
                event.setSalesChannel(rs2.getString("sales_channel"));
                event.setSalesStartTime(String.valueOf(rs2.getTimestamp("sale_start_time").toLocalDateTime()));
                event.setSalesEndTime(String.valueOf(rs2.getTimestamp("sale_end_time").toLocalDateTime()));
                event.setOrganizationId(rs2.getLong("organization_id"));
                event.setTicketed(true);
                event.setEventPaymentType(rs2.getString("sales_channel"));
            } else if (Objects.equals(rs.getString("ticketed_type").toLowerCase(), "unticketed")) {
                event.setTicketed(false);
                query = "SELECT * FROM unticketedevent WHERE event_id = ?";
                stmt = conn.prepareStatement(query);
                stmt.setLong(1, eventId);
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();

                event.setCapacity(String.valueOf(rs2.getInt("capacity")));
                event.setUserId(rs2.getLong("user_id"));
            }
            FormatStyle dateStyle = FormatStyle.MEDIUM;
            FormatStyle timeStyle = FormatStyle.SHORT;
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle);

            event.setEventId(rs.getLong("event_id"));
            event.setTitle(rs.getString("event_name"));
            event.setVenueLocation(rs.getString("location_name"));
            event.setAddress(rs.getString("address_description"));
            event.setState(rs.getString("state"));
            event.setCity(rs.getString("city"));
            event.setCountry(rs.getString("country"));
            event.setPostalCode(rs.getString("postal_code"));
            event.setLatitude(String.valueOf(rs.getDouble("latitude")));
            event.setLongitude(String.valueOf(rs.getDouble("longitude")));
            event.setStartdate(String.valueOf(rs.getTimestamp("start_date").toLocalDateTime().format(formatter)));
            event.setEnddate(String.valueOf(rs.getTimestamp("end_date").toLocalDateTime().format(formatter)));
            event.setMinimumAge(String.valueOf(rs.getInt("minimum_age")));

            event.setLocationType(rs.getString("is_online"));
            event.setEventDescription(rs.getString("description"));
            event.setImageUrl(rs.getString("image_url"));
            event.setLocationName(rs.getString("location_name"));

            event.setEventTypes(new ArrayList<>());
            event.getEventTypes().add(rs.getString("type_of_event"));

            first = false;
        }
        return event;
    }

    public HashMap<String, ArrayList<EventModel>> getAllEvents() throws SQLException {
        Connection conn = super.getConnection();

        if (conn == null)
            throw new SQLException("Connection to the database failed");

        HashMap<String, ArrayList<EventModel>> eventMap = new HashMap<>();
        eventMap.put("past", new ArrayList<>());
        eventMap.put("future", new ArrayList<>());

        String query = "SELECT E.event_id, E.start_date, E.event_name, E.image_url, " +
                        "E.is_online, L.location_name, ET.type_of_event, E.current_state " +
                "FROM Event E " +
                "LEFT OUTER JOIN Location L ON E.event_id = L.event_id " +
                "LEFT OUTER JOIN event_type ET ON E.event_id = ET.event_id";

        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        ArrayList<EventModel> events = new ArrayList<>();

        while (rs.next()) {
            long id = rs.getLong("event_id");
            if (events.stream().anyMatch(event -> event.getEventId() == id)) {
                events.stream().filter(event -> event.getEventId() == id).findFirst().get().getEventTypes().add(rs.getString("type_of_event"));
                continue;
            }

            EventModel event = new EventModel();
            FormatStyle dateStyle = FormatStyle.MEDIUM;
            FormatStyle timeStyle = FormatStyle.SHORT;
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle);
            event.setEventId(rs.getLong("event_id"));
            event.setStartdate(rs.getTimestamp("start_date").toLocalDateTime().format(formatter));
            event.setTitle(rs.getString("event_name"));
            event.setImageUrl(rs.getString("image_url"));
            event.setState(rs.getString("current_state"));
            event.setEventTypes(new ArrayList<>());
            event.getEventTypes().add(rs.getString("type_of_event"));

            if (rs.getBoolean("is_online"))
                event.setLocationName("Online");
            else
                event.setLocationName(rs.getString("location_name"));
            events.add(event);
        }
        for(EventModel eventModel: events){
            if(eventModel.getState().equals("ONGOING") || eventModel.getState().equals("UPCOMING"))
                eventMap.get("future").add(eventModel);
            else if(eventModel.getState().equals("FINISHED"))
                eventMap.get("past").add(eventModel);
        }
        return eventMap;
    }

    public ArrayList<EventModel> getFilteredEvents(FilterModel filterModel) throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null) {
            throw new SQLException("Connection to the database failed");
        }
        String query = "SELECT E.event_id, E.start_date, E.event_name, E.image_url,  E.is_online, ET.type_of_event, L.location_name " +
                "FROM Event E LEFT OUTER JOIN event_type ET ON E.event_id = ET.event_id " +
                "LEFT OUTER JOIN Location L ON E.event_id = L.event_id " +
                "WHERE " +
                "(? is null OR UPPER(E.event_name) LIKE UPPER(?) OR " +
                "? is null OR UPPER(L.country) LIKE UPPER(?) OR " +
                "? is null OR UPPER(L.city) LIKE UPPER(?) OR " +
                "? is null OR UPPER(L.state) LIKE UPPER(?) OR " +
                "? is null OR UPPER(L.postal_code) LIKE UPPER(?) OR " +
                "? is null OR UPPER(L.location_name) LIKE UPPER(?) )  AND " +
                "(?::timestamp is null OR E.start_date >= ?) AND " +
                "(?::timestamp is null OR E.end_date <= ?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        for (int x = 1; x < 13; x++) {
            stmt.setString(x, "%" + filterModel.getName() + "%");
        }
        //check if filterModel.getStartDate() equals "" and set parameter 13 to null if it is true. Else set it to the value of filterModel.getStartDate()
        if (filterModel.getStartDate().equals("")) {
            stmt.setTimestamp(13, null);
            stmt.setTimestamp(14, null);
            stmt.setTimestamp(15, null);
            stmt.setTimestamp(16, null);

        } else {
            stmt.setTimestamp(13, Timestamp.valueOf(filterModel.getStartDate().split("T")[0] + " " + filterModel.getStartDate().split("T")[1] + ":00"));
            stmt.setTimestamp(14, Timestamp.valueOf(filterModel.getStartDate().split("T")[0] + " " + filterModel.getStartDate().split("T")[1] + ":00"));
            stmt.setTimestamp(15, (!filterModel.getEndDate().equals("") ? (Timestamp.valueOf(filterModel.getStartDate().split("T")[0] + " " + filterModel.getStartDate().split("T")[1] + ":00")) : null));
            stmt.setTimestamp(16, (!filterModel.getEndDate().equals("") ? (Timestamp.valueOf(filterModel.getEndDate().split("T")[0] + " " + filterModel.getEndDate().split("T")[1] + ":00")) : null));
        }

        ResultSet rs = stmt.executeQuery();

        ArrayList<EventModel> events = new ArrayList<>();

        while (rs.next()) {
            long id = rs.getLong("event_id");
            if (events.stream().anyMatch(event -> event.getEventId() == id)) {
                events.stream().filter(event -> event.getEventId() == id).findFirst().get().getEventTypes().add(rs.getString("type_of_event"));
                continue;
            }
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
            event.setEventTypes(new ArrayList<>());
            event.getEventTypes().add(rs.getString("type_of_event"));
            events.add(event);
        }
        if(filterModel.getEventTypes() != null){
            ArrayList<EventModel> filteredEvents = new ArrayList<>();

            for (String type : filterModel.getEventTypes()) {
                for (EventModel eventModel : events) {
                    if (eventModel.getEventTypes().contains(type)) {
                        filteredEvents.add(eventModel);
                    }
                }
            }
            return filteredEvents;
        }
        else{
            return events;
        }

    }
    @Scheduled(fixedRate = 1000*60*60)
    public void updateEventStates() throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null) {
            throw new SQLException("Connection to the database failed");
        }
        String query = "UPDATE Event SET current_state = 'ONGOING' WHERE start_date <= now() AND end_date >= now();\n" +
                "UPDATE Event SET current_state = 'FINISHED' WHERE end_date < now();\n" +
                "UPDATE Event SET current_state = 'UPCOMING' WHERE start_date > now();";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.execute();
    }

}
