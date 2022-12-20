package com.database.eventmania.backend.service;

import com.database.eventmania.backend.DTO.EventDTO;
import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.SalesChannel;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import com.database.eventmania.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UnticketedEventService unticketedEventService;
    private final TicketedEventService ticketedEventService;

    @Autowired
    public EventService(EventRepository eventRepository, UnticketedEventService unticketedEventService, TicketedEventService ticketedEventService) {
        this.eventRepository = eventRepository;
        this.unticketedEventService = unticketedEventService;
        this.ticketedEventService = ticketedEventService;
    }

    private Long eventId;
    private Long adminId;
    private String feedback;
    private LocalDateTime verificationDate;
    private VerificationStatus verificationStatus;
    private String eventName;
    private String eventDescription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isOnline;
    private String imageUrl;
    private Integer minimumAge;
    private EventState currentState;
    private EventType eventType;

    /*public boolean createEvent(boolean isTicketed, VerificationStatus verificationStatus, String eventName, String eventDescription,
                               LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                               Integer minimumAge, EventState currentState, ArrayList<EventType> eventTypes, SalesChannel salesChannel,
                               LocalDateTime saleStartTime, LocalDateTime saleEndTime, Long userId,
                               Integer capacity, String locationName, Float latitude, Float longitude,
                               String postalCode, String state, String city, String country,
                               String addressDescription) throws SQLException {

        return eventRepository.createEvent(isTicketed, verificationStatus, eventName,
                eventDescription, startDate, endDate, isOnline, imageUrl, minimumAge, currentState, eventTypes, salesChannel,
                saleStartTime, saleEndTime, userId, capacity, locationName, latitude, longitude, postalCode, state, city,
                country, addressDescription, eventCreatorId);
    }*/

    public boolean deleteEvent(Long eventId) throws SQLException {
        return eventRepository.deleteEvent(eventId);
    }

    public EventDTO getEventById(Long eventId) throws SQLException {
        return eventRepository.getEventById(eventId);
    }

    public ArrayList<EventDTO> getAllEvents() throws SQLException {
        return eventRepository.getAllEvents();
    }

    private void savePhoto() {

    }
}
