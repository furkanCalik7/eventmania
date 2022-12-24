package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.SQLException;

@RestController
@RequestMapping(path = "user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
}
