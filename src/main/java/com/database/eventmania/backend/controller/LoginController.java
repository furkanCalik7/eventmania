package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.service.AuthenticationService;
import com.database.eventmania.backend.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/login")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {
    private AuthenticationService authenticationService;
    private UserService userService;

    public LoginController(AuthenticationService authenticationService,
                           UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

//    @PostMapping
//    public ResponseEntity<Map<String, Object>> login(String email, String password) {
//        Map<String, Object> returnMap = authenticationService.authenticateAccount(email, password);
//        if(returnMap.get("token") == null) {
//            return new ResponseEntity<>(returnMap, HttpStatus.NOT_ACCEPTABLE);
//        }
//        return new ResponseEntity<>(returnMap, HttpStatus.OK);
//    }
}