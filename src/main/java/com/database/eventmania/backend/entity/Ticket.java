package com.database.eventmania.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Ticket {
    private Long ticketId;
    private Long eventId;
    private Long userId;
    private LocalDateTime transactionDate;
    private String categoryName;
    private SalesChannel purchaseType;
}
