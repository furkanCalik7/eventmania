package com.database.eventmania.backend.entity;

import com.database.eventmania.backend.entity.enums.ReportState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class Report {
    private Long reportId;
    private Long eventId;
    private Long userId;
    private String description;
    private String reportType;
    private ReportState reportState;
    private LocalDateTime reportDate;
    private ArrayList<String> screenshotUrls;
}

