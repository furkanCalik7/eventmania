package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.entity.Category;
import com.database.eventmania.backend.model.CategoryModel;
import com.database.eventmania.backend.model.EventModel;
import com.database.eventmania.backend.model.FilterModel;
import com.database.eventmania.backend.service.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("event")
public class EventController {

    private TicketedEventService ticketedEventService;
    private UnticketedEventService unticketedEventService;
    private EventService eventService;
    private CategoryService categoryService;
    private UserService userService;

    public EventController(TicketedEventService ticketedEventService, UnticketedEventService unticketedEventService, EventService eventService, CategoryService categoryService, UserService userService) {
        this.ticketedEventService = ticketedEventService;
        this.unticketedEventService = unticketedEventService;
        this.eventService = eventService;
        this.categoryService = categoryService;
        this.userService = userService;
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
                return new ModelAndView("redirect:/event/" + eventId + "/tickets");
            } else {
                unticketedEventService.createUnticketedEvent(eventModel, principal.getName());
                return new ModelAndView("redirect:/user/events");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //get mapping with filter model class
    @PostMapping("filter")
    public ModelAndView listEventPage(@ModelAttribute("filterModel") FilterModel filterModel) throws SQLException {
        ModelAndView mav = new ModelAndView("frontend/event/list_event.html");
        try {
            mav.addObject("events", eventService.getFilteredEvents(filterModel));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @GetMapping("{eventId}/information")
    public ModelAndView getEventInformation(@PathVariable("eventId") String eventId, Principal principal) throws SQLException {
        ModelAndView mav = new ModelAndView("frontend/event/eventInformation.html");
        boolean joined = userService.isUserInEvent(eventId, principal.getName());
        EventModel eventModel = null;
        try {
            eventModel = eventService.getEventById(Long.valueOf(eventId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.addObject("joined", joined);
        mav.addObject("event", eventModel);
        return mav;
    }

    @GetMapping("/{eventId}/tickets")
    public ModelAndView ticketPage(@PathVariable(value = "eventId") final String eventId) throws SQLException {
        ModelAndView mav = new ModelAndView("frontend/event/event_tickets.html");
        EventModel eventModel = eventService.getEventById(Long.parseLong(eventId));
        mav.addObject("event", eventModel);
        ArrayList<Category> categories = categoryService.getCategoriesByEventId(eventId);
        mav.addObject("categories", categories);
        CategoryModel categoryModel = new CategoryModel();
        mav.addObject("model", categoryModel);
        return mav;
    }

    @PostMapping("/{eventId}/create/category")
    public ModelAndView createCategory(@PathVariable("eventId") String eventId, ModelMap modelMap, CategoryModel categoryModel) {
        try {
            categoryService.addCategory(eventId, categoryModel.getName(), categoryModel.getDesc(), categoryModel.getCapacity(), categoryModel.getPrice());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/event/" + eventId + "/tickets", modelMap);
    }

    @GetMapping("/{eventId}/category")
    public ModelAndView categoryPage(@PathVariable(value = "eventId") final String eventId) throws SQLException {
        ModelAndView mav = new ModelAndView("frontend/event/buy_ticket_model.html");
        EventModel eventModel = eventService.getEventById(Long.parseLong(eventId));
        ArrayList<CategoryModel> categoryModels = categoryService.getCategoriesByEventIdWithCapacityCheck(eventId);

        ArrayList<CategoryModel2> categoryNames = new ArrayList<>();
        for (CategoryModel categoryModel : categoryModels) {
            CategoryModel2 categoryModel2 = new CategoryModel2();
            categoryModel2.setId(categoryModel.getName());
            categoryModel2.setName(categoryModel.getName() + " - " + categoryModel.getPrice() + " TL - Remaining Capacity: " + categoryModel.getRemainingCapacity());
            categoryNames.add(categoryModel2);
        }
        mav.addObject("event", eventModel);
        mav.addObject("categories", categoryNames);
        return mav;
    }

    @PostMapping("/{eventId}/publish")
    public ModelAndView publishEvent(@PathVariable("eventId") String eventId, ModelMap modelMap, CategoryModel categoryModel) {
        try {
            // TODO: Fix the frontend here
            eventService.publishTicketedEvent(eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/event/" + eventId + "/tickets", modelMap);
    }

    @PostMapping("{eventId}/join")
    public ModelAndView joinEvent(@PathVariable("eventId") String eventId, Principal principal) {
        try {
            userService.joinUnticketedEvent(principal.getName(), eventId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/event/" + eventId + "/information");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public class CategoryModel2 {
        private String name;
        private String id;
    }
}
