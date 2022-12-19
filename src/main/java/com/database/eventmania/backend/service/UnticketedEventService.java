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
    public boolean createUnticketedEvent(EventModel eventModel) throws IOException {
        boolean isOnline = eventModel.getLocationType().equals("online");
        LocalDateTime startDate = LocalDateTime.parse(eventModel.getStartdate());
        LocalDateTime endDate = LocalDateTime.parse(eventModel.getEnddate());
        ArrayList<EventType> eventTypes = new ArrayList<>();
        for (String eventType : eventModel.getEventTypes()) {
            eventTypes.add(EventType.valueOf(eventType));
        }
        SalesChannel salesChannel = SalesChannel.valueOf(eventModel.getSalesChannel());
        float latitude = 0, longitude = 0;
        if (!isOnline) {
            latitude = Float.parseFloat(eventModel.getLatitude());
            longitude = Float.parseFloat(eventModel.getLongitude());
        }

        int minimumAge = Integer.parseInt(eventModel.getMinimumAge());
        Utils.copyFile(eventModel.getFile());
        return unticketedEventRepository.createUnticketedEvent( VerificationStatus.UNDER_REVIEW, eventModel.getTitle(), eventModel.getEventDescription(),
                startDate, endDate, isOnline, eventModel.getFile().getName(), minimumAge,
                EventState.UPCOMING, eventTypes, eventModel.getUserId(),
                eventModel.getVenueLocation(), latitude, longitude,
                eventModel.getPostalCode(), eventModel.getState(), eventModel.getCity(),  eventModel.getCountry(),
                eventModel.getAddress());
        }

    }
    /*public boolean createUnticketedEvent(Long adminId, String feedback, LocalDateTime verificationDate,
                                         VerificationStatus verificationStatus, String eventName, String eventDescription,
                                         LocalDateTime startDate, LocalDateTime endDate, Boolean isOnline, String imageUrl,
                                         Integer minimumAge, EventState currentState, ArrayList<EventType> eventTypes,
                                         Long userId, Integer capacity, String locationName, Float latitude, Float longitude,
                                         String postalCode, String state, String city, String country,
                                         String addressDescription) throws SQLException {
        return unticketedEventRepository.createUnticketedEvent(verificationStatus, eventName,
                eventDescription, startDate, endDate, isOnline, imageUrl, minimumAge, currentState, eventTypes, userId, capacity,
                locationName, latitude, longitude, postalCode, state, city, country, addressDescription);
    }*/
}
