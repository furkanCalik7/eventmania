package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.model.EventModel;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

@Repository
public class ReportRepository extends BaseRepository {
    public ReportRepository() {
        super.connect();
    }

    //Gets the users who have joined the most events. The number of joins is returned by the age property of BasicUser
    public ArrayList<BasicUser> getMostActiveUsers() throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null) {
            throw new SQLException("Connection is null");
        }
        String query = "SELECT U.user_id, U.email, U.first_name, U.last_name, " +
                "U.phone_number, count(*) as count " +
                "FROM event E NATURAL JOIN join_event NATURAL JOIN basicuser U " +
                "GROUP BY U.user_id, U.email, U.first_name, U.last_name " +
                "ORDER BY count DESC";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        ArrayList<BasicUser> users = new ArrayList<>();
        while(rs.next()){
            BasicUser user = new BasicUser();
            user.setAccountId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setAge(rs.getInt("count"));
            users.add(user);
        }
        return users;
    }

    //TODO: may need to limit to 10
    //Gets the events that have the most attendees, sets the number of attendees as the attendeeCount property of EventModel
    public ArrayList<EventModel> getMostPopularEvents() throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null) {
            throw new SQLException("Connection is null");
        }
        String query = "SELECT E.event_id, E.event_name, E.start_date, E.end_date, count(*) as count " +
                "FROM event E NATURAL JOIN join_event " +
                "GROUP BY E.event_id, E.event_name, E.start_date, E.end_date " +
                "ORDER BY count DESC";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        ArrayList<EventModel> events = new ArrayList<>();

        FormatStyle dateStyle = FormatStyle.MEDIUM;
        FormatStyle timeStyle = FormatStyle.SHORT;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle);
        while(rs.next()){
            EventModel event = new EventModel();
            event.setEventId(rs.getLong("event_id"));
            event.setTitle(rs.getString("event_name"));
            event.setStartdate(rs.getTimestamp("start_date").toLocalDateTime().format(formatter));
            event.setEnddate(rs.getTimestamp("end_date").toLocalDateTime().format(formatter));
            event.setAttendeeCount(rs.getInt("count"));
            events.add(event);
        }
        return events;
    }

}
