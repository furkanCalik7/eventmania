package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.model.BasicUserRegisterModel;
import com.database.eventmania.backend.model.OrganizationRegisterModel;
import com.database.eventmania.backend.service.OrganizationService;
import com.database.eventmania.backend.service.UserService;
import org.springframework.boot.Banner;
import org.springframework.ui.ModelMap;
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

    @GetMapping("/user")
    public ModelAndView registerPage() {
        ModelAndView mav = new ModelAndView("register.html");
        BasicUserRegisterModel registerModel = new BasicUserRegisterModel();
        mav.addObject("registerModel", registerModel);
        return mav;
    }

    @GetMapping("organization")
    public ModelAndView registerOrgPage() {
        ModelAndView mav = new ModelAndView("org_register.html");
        OrganizationRegisterModel registerModel = new OrganizationRegisterModel();
        mav.addObject("registerModel", registerModel);
        return mav;
    }

    // TODO: register organization
    @PostMapping()
    public ModelAndView registerBasicUser(@ModelAttribute("registerModel") BasicUserRegisterModel registerModel, ModelMap modelMap) {
        try {
            userService.saveUser(
                    registerModel.getEmail(),
                    registerModel.getPassword(),
                    registerModel.getFirstName(),
                    registerModel.getLastName(),
                    registerModel.getGender(),
                    registerModel.getPhoneNumber(),
                    registerModel.getDob()
            );
            modelMap.addAttribute("result", "success");
        } catch (SQLException e) {
            if (e.getSQLState() == "23505") modelMap.addAttribute("result", "alreadyExists");
            else modelMap.addAttribute("result", "error");
        }
        return new ModelAndView("redirect:/register/user", modelMap);
    }

    @PostMapping("organization")
    public ModelAndView registerOrganization(@ModelAttribute("registerModel") OrganizationRegisterModel registerModel, ModelMap modelMap) {
        try {
            organizationService.saveOrganization(
                    registerModel.getEmail(),
                    registerModel.getPassword(),
                    registerModel.getOrganizationName(),
                    registerModel.getDescription(),
                    registerModel.getPhoneNumber()
            );
            modelMap.addAttribute("result", "success");
        } catch (SQLException e) {
            if (e.getSQLState() == "23505") modelMap.addAttribute("result", "alreadyExists");
            else modelMap.addAttribute("result", "error");
        }
        return new ModelAndView("redirect:/register/organization", modelMap);
    }
}
