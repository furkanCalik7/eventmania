package com.database.eventmania.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class EventModel {
    public EventModel() {
        this.eventTypes = new ArrayList<>();
    }
    private Long eventId;
    private String title;
    private ArrayList<String> eventTypes;
    private String venueLocation;
    private String address;
    private String state;
    private String city;
    private String country;
    private String postalCode;
    private String longitude;
    private String latitude;
    private String startdate;
    private String enddate;
    private MultipartFile file;
    private String minimumAge;
    private String salesChannel;
    private String capacity;
    private String locationType;
    private String eventPaymentType;
    private String eventDescription;
    private String salesStartTime;
    private String salesEndTime;
    private String imageUrl;

    //if the event is online, the locationName is "online"
    private String locationName;
    private boolean isTicketed;
    private Long organizationId;

    private Long userId;
    private Integer attendeeCount;
    private Integer ticketCount;
}

