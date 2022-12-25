package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class ReportService {
    ReportRepository reportRepository;
    @Autowired
    public ReportService(ReportRepository reportRepository){
        this.reportRepository = reportRepository;
    }
    public ArrayList<BasicUser> getMostActiveUsers() throws SQLException {
        return reportRepository.getMostActiveUsers();
    }
}
