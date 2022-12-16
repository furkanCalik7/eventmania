package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.model.EventModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("event")
public class EventController {

    @GetMapping("create")
    public ModelAndView createEventPage() {
        ModelAndView mav = new ModelAndView("frontend/event/create_event.html");
        EventModel eventModel = new EventModel();
        mav.addObject("eventModel", eventModel);
        return mav;
    }

    @PostMapping("create")
    public String createEvent(@ModelAttribute("eventModel") EventModel eventModel) {
        // Create event section
        // TODO: Get event information in the model
        // TODO: upload img to under /static/img with a naming that specifies the event id
        return "";
    }
}
