package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.model.BasicUserRegisterModel;
import com.database.eventmania.backend.model.RegisterModel;
import com.database.eventmania.backend.service.OrganizationService;
import com.database.eventmania.backend.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@RestController
@RequestMapping(path = "/register")
public class RegisterController {

    private UserService userService;
    private OrganizationService organizationService;

    public RegisterController(UserService userService, OrganizationService organizationService) {
        this.userService = userService;
        this.organizationService = organizationService;
    }

    // TODO: different login page functions for different user types
    @GetMapping()
    public ModelAndView loginPage() {
        ModelAndView mav = new ModelAndView("register.html");
        BasicUserRegisterModel registerModel = new BasicUserRegisterModel();
        mav.addObject("registerModel", registerModel);
        return mav;
    }

    // TODO: register organization
    @PostMapping()
    public String registerBasicUser(@ModelAttribute("registerModel") BasicUserRegisterModel registerModel) throws SQLException {
        userService.saveUser(
                registerModel.getEmail(),
                registerModel.getPassword(),
                registerModel.getFirstName(),
                registerModel.getLastName(),
                registerModel.getGender(),
                registerModel.getPhoneNumber(),
                registerModel.getDob()
                );
        return "";
    }

    @PostMapping()
    public String registerOrganization(@ModelAttribute("registerModel") OrganizationRegisterModel registerModel) throws SQLException {
        // organizationService.saveOrganization(

        //         );
        return "";
    }
}
