package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.model.RegisterModel;
import com.database.eventmania.backend.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@RestController
@RequestMapping(path = "/register")
public class RegisterController {

    private UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ModelAndView loginPage() {
        ModelAndView mav = new ModelAndView("register.html");
        RegisterModel registerModel = new RegisterModel();
        mav.addObject("registerModel", registerModel);
        return mav;
    }

    @PostMapping()
    public String register(@ModelAttribute("registerModel") RegisterModel registerModel) throws SQLException {
        userService.saveUser(registerModel.getUsername(), registerModel.getPassword());
        return "";
    }
}
