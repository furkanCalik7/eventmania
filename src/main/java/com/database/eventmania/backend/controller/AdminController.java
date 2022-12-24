package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.entity.Admin;
import com.database.eventmania.backend.model.LoginModel;
import com.database.eventmania.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@RestController
@RequestMapping(path = "admin")
public class AdminController {
    @GetMapping("login")
    public ModelAndView loginPage() {
        ModelAndView mav = new ModelAndView("admin/login.html");
        LoginModel loginModel = new LoginModel();
        mav.addObject("login", loginModel);
        return mav;
    }

    @PostMapping()
    public String login(@RequestParam(value = "access") String accessToken, @ModelAttribute("loginModal") LoginModel loginModel) {
        // TODO: ADD LOGIN METHOD HERE
        return "";
    }
}
