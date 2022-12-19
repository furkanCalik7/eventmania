package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.model.EventModel;
import com.database.eventmania.backend.service.TicketedEventService;
import com.database.eventmania.backend.service.UnticketedEventService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
@RequestMapping("event")
public class EventController {

    private TicketedEventService ticketedEventService;
    private UnticketedEventService unticketedEventService;

    public EventController(TicketedEventService ticketedEventService, UnticketedEventService unticketedEventService) {
        this.ticketedEventService = ticketedEventService;
        this.unticketedEventService = unticketedEventService;
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
