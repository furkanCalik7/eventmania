package com.database.eventmania.backend.service;

import com.database.eventmania.backend.Utils;
import com.database.eventmania.backend.entity.enums.EventState;
import com.database.eventmania.backend.entity.enums.EventType;
import com.database.eventmania.backend.entity.enums.SalesChannel;
import com.database.eventmania.backend.entity.enums.VerificationStatus;
import com.database.eventmania.backend.model.EventModel;
import com.database.eventmania.backend.repository.TicketedEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class TicketedEventService {
    private final TicketedEventRepository ticketedEventRepository;

    @Autowired
    public TicketedEventService(TicketedEventRepository ticketedEventRepository) {
        this.ticketedEventRepository = ticketedEventRepository;
    }

    public boolean createTicketedEvent(EventModel eventModel, String email) throws SQLException, IOException {
        boolean isOnline = eventModel.getLocationType().equals("online");
        LocalDateTime startDate = LocalDateTime.parse(eventModel.getStartdate());
        LocalDateTime endDate = LocalDateTime.parse(eventModel.getEnddate());
        LocalDateTime saleStartTime = LocalDateTime.parse(eventModel.getSalesStartTime());
        LocalDateTime saleEndTime = LocalDateTime.parse(eventModel.getSalesEndTime());
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
        return ticketedEventRepository.createTicketedEvent(VerificationStatus.UNDER_REVIEW, eventModel.getTitle(), eventModel.getEventDescription(),
                startDate, endDate, isOnline, eventModel.getFile().getName(), minimumAge,
                EventState.UPCOMING, eventTypes, salesChannel, saleStartTime,
                saleEndTime, eventModel.getVenueLocation(), latitude, longitude,
                eventModel.getPostalCode(), eventModel.getState(), eventModel.getCity(), eventModel.getCountry(),
                eventModel.getAddress(), email);
    }
}
