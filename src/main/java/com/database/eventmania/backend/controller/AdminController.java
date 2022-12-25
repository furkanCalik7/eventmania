package com.database.eventmania.backend.controller;

import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.model.EventModel;
import com.database.eventmania.backend.service.ReportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "admin")
public class AdminController {
    private ReportService reportService;

    public AdminController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("dashboard")
    public ModelAndView dashboard() {
        ModelAndView mav = new ModelAndView("admin/dashboard.html");
        ArrayList<BasicUser> report1 = null;
        ArrayList<EventModel> report2 = null;
        try {
            report1 = reportService.getMostActiveUsers();
            report2 = reportService.getMostPopularEvents();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        mav.addObject("report1", report1);
        mav.addObject("report2", report2);
        return mav;
    }
}
