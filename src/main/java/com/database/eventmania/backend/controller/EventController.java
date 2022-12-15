package com.database.eventmania.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("event")
public class EventController {

    @GetMapping("create")
    public ModelAndView createEvent() {
        ModelAndView mav = new ModelAndView("frontend/event/create_event.html");
        return mav;
    }
}
