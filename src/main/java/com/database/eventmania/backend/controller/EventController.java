package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.DTO.EventDTO;
import com.database.eventmania.backend.model.EventModel;
import com.database.eventmania.backend.model.FilterModel;
import com.database.eventmania.backend.service.CategoryService;
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
    private CategoryService categoryService;

    public EventController(TicketedEventService ticketedEventService, UnticketedEventService unticketedEventService, EventService eventService, CategoryService categoryService) {
        this.ticketedEventService = ticketedEventService;
        this.unticketedEventService = unticketedEventService;
        this.eventService = eventService;
        this.categoryService = categoryService;
    }

    @GetMapping("create")
    public ModelAndView createEventPage() {
        ModelAndView mav = new ModelAndView("frontend/event/create_event.html");
        EventModel eventModel = new EventModel();
        mav.addObject("eventModel", eventModel);
        return mav;
    }

    @PostMapping("create")
    public ModelAndView createEvent(Principal principal, @ModelAttribute("eventModel") EventModel eventModel) {
        Long eventId = 0L;
        try {
            if (eventModel.getEventPaymentType().equals("PAID")) {
                eventId = ticketedEventService.createTicketedEvent(eventModel, principal.getName());
            } else {
                eventId = unticketedEventService.createUnticketedEvent(eventModel, principal.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/event/" + eventId + "/tickets");
    }

    //get mapping with filter model class
    @PostMapping("filter")
    public ModelAndView listEventPage(@ModelAttribute("filterModel") FilterModel filterModel) throws SQLException {
        ModelAndView mav = new ModelAndView("frontend/event/list_event.html");
        try {
//            eventService.getFilteredEvents(filterModel)
            mav.addObject("events", eventService.getAllEvents().remove(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @GetMapping("{eventId}/information")
    public ModelAndView getEventInformation(@PathVariable("eventId") String eventId) throws SQLException {
        ModelAndView mav = new ModelAndView("frontend/event/eventInformation.html");
        EventDTO eventModel = null;
        try {
            eventModel = eventService.getEventById(Long.valueOf(eventId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.addObject("event", eventModel.getMap().get("event"));
    @GetMapping("/{eventId}/tickets")
    public ModelAndView ticketPage(@PathVariable(value = "eventId") final String eventId) throws SQLException {
        ModelAndView mav = new ModelAndView("frontend/event/event_tickets.html");
        EventDTO eventDTO = eventService.getEventById(Long.parseLong(eventId));
        mav.addObject("event", eventDTO.getMap().get("event"));
//        ArrayList<CategoryModel> categories = .getCategoriesByEventId(eventId);
        return mav;
    }
}
