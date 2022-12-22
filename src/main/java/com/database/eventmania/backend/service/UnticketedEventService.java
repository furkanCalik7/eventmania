package com.database.eventmania.backend.service;

import com.database.eventmania.backend.Utils;
import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.SalesChannel;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import com.database.eventmania.backend.model.EventModel;
import com.database.eventmania.backend.repository.UnticketedEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class UnticketedEventService {
    private final UnticketedEventRepository unticketedEventRepository;

    @Autowired
    public UnticketedEventService(UnticketedEventRepository unticketedEventRepository) {
        this.unticketedEventRepository = unticketedEventRepository;
    }

    public Long createUnticketedEvent(EventModel eventModel, String email) throws IOException, SQLException {
        boolean isOnline = eventModel.getLocationType().toLowerCase().equals("online");
        LocalDateTime startDate = LocalDateTime.parse(eventModel.getStartdate());
        LocalDateTime endDate = LocalDateTime.parse(eventModel.getEnddate());
        ArrayList<EventType> eventTypes = new ArrayList<>();
        for (String eventType : eventModel.getEventTypes()) {
            eventTypes.add(EventType.valueOf(eventType));
        }
        float latitude = 0, longitude = 0;
        if (!isOnline) {
            latitude = Float.parseFloat(eventModel.getLatitude());
            longitude = Float.parseFloat(eventModel.getLongitude());
        }

        int minimumAge = Integer.parseInt(eventModel.getMinimumAge());
        Utils.copyFile(eventModel.getFile());
        return unticketedEventRepository.createUnticketedEvent(VerificationStatus.UNDER_REVIEW, eventModel.getTitle(), eventModel.getEventDescription(),
                startDate, endDate, isOnline, eventModel.getFile().getOriginalFilename(), minimumAge,
                EventState.UPCOMING, eventTypes, Integer.valueOf(eventModel.getCapacity()),
                eventModel.getVenueLocation(), latitude, longitude,
                eventModel.getPostalCode(), eventModel.getState(), eventModel.getCity(), eventModel.getCountry(),
                eventModel.getAddress(), email);
    }
}