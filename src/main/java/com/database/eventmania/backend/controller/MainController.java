package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class MainController {
    @GetMapping()
    public ModelAndView homepage() {
        ModelAndView mav = new ModelAndView("frontend/homepage.html");
        return mav;
    }
    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }
}
