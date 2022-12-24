package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.model.EventModel;
import com.database.eventmania.backend.model.FilterModel;
import com.database.eventmania.backend.service.EventService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/")
public class MainController {
    private EventService eventService;

    public MainController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping()
    public ModelAndView homepage() {
        ModelAndView mav = new ModelAndView("frontend/homepage.html");
        HashMap<String, ArrayList<EventModel>> eventModels = null;
        try {
            eventModels = eventService.getAllEvents();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mav.addObject("events", eventModels);
        mav.addObject("filterModel", new FilterModel());
        return mav;
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }
}
