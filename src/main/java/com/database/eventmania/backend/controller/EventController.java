package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.model.EventModel;
import com.database.eventmania.backend.model.FilterModel;
import com.database.eventmania.backend.service.EventService;
import com.database.eventmania.backend.service.TicketedEventService;
import com.database.eventmania.backend.service.UnticketedEventService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.SQLException;

@RestController
@RequestMapping("event")
public class EventController {

    private TicketedEventService ticketedEventService;
    private UnticketedEventService unticketedEventService;
    private EventService eventService;

    public EventController(TicketedEventService ticketedEventService, UnticketedEventService unticketedEventService, EventService eventService) {
        this.ticketedEventService = ticketedEventService;
        this.unticketedEventService = unticketedEventService;
        this.eventService = eventService;
    }

    @GetMapping("create")
    public ModelAndView createEventPage() {
        ModelAndView mav = new ModelAndView("frontend/event/create_event.html");
        EventModel eventModel = new EventModel();
        mav.addObject("eventModel", eventModel);
        return mav;
    }

    @PostMapping("create")
    public String createEvent(Principal principal, @ModelAttribute("eventModel") EventModel eventModel) {
        try {
            if (eventModel.getEventPaymentType().equals("PAID")) {
                ticketedEventService.createTicketedEvent(eventModel, principal.getName());
            } else {
                unticketedEventService.createUnticketedEvent(eventModel, principal.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //get mapping with filter model class
    @GetMapping()
    public ModelAndView listEventPage(@ModelAttribute("filterModel") FilterModel filterModel) throws SQLException {
        ModelAndView mav = new ModelAndView("frontend/event/list_event.html");
        try {
            eventService.getAllEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}
