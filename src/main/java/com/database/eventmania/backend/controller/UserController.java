package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.model.EventModel;
import com.database.eventmania.backend.service.EventService;
import com.database.eventmania.backend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "user")
public class UserController {
    private UserService userService;
    private EventService eventService;

    public UserController(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping("profile")
    public ModelAndView userProfilePage(Principal principal) {
        ModelAndView mav = new ModelAndView("frontend/user_profile/userProfile.html");
        BasicUser user = null;
        try {
            user = userService.getUserByEmail(principal.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mav.addObject("user", user);
        return mav;
    }

    @GetMapping("events")
    public ModelAndView eventsPage(Principal principal) {
        ModelAndView mav = new ModelAndView("frontend/user_profile/my_events.html");
        ArrayList<EventModel> organized = null;
        ArrayList<EventModel> joined = null;
        ArrayList<EventModel> future = null;
        try {
            joined = userService.listJoinedEvents(principal.getName());
            future = userService.listFutureEvents(principal.getName());
            organized = userService.getOrganizedEvents(principal.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mav.addObject("joined", joined);
        mav.addObject("future", future);
        mav.addObject("organized", organized);
        return mav;
    }
}
