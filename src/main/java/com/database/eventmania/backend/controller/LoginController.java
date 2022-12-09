package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.model.LoginModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(path = "/login")
public class LoginController {
    @GetMapping()
    public ModelAndView loginPage() {
        ModelAndView mav = new ModelAndView("login.html");
        LoginModel loginModel = new LoginModel();
        mav.addObject("login", loginModel);
        // Change
        mav.addObject("access_token", "test");
        return mav;
    }

    @PostMapping()
    public String login(@RequestParam(value = "access") String accessToken, @ModelAttribute("loginModal") LoginModel loginModel) {
        // TODO: ADD LOGIN METHOD HERE
        return "";
    }
}