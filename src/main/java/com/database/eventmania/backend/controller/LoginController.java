package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.model.LoginModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(path = "/login")
public class LoginController {
    @GetMapping()
    public ModelAndView loginPage() {
        ModelAndView mav = new ModelAndView("login.html");
        return mav;
    }
}