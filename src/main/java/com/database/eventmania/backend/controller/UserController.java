package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping(path = "user")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping(path = "getUser/{userId}")
    public BasicUser getUserById(@PathVariable("userId") Long userId) throws SQLException {
        return userService.getUserById(userId);
    }

}