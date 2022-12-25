package com.database.eventmania.backend.service;

import com.database.eventmania.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class TicketService {
    public TicketRepository ticketRepository;
    @Autowired
    public TicketService(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }
    public boolean createTicket(String eventId, String userId, String categoryName, String purchaseType) throws SQLException {
        return ticketRepository.createTicket(Long.parseLong(eventId), Long.parseLong(userId), categoryName, purchaseType);
    }
    public boolean buyTicket(String eventId, String userId, String categoryName, String purchaseType) throws SQLException {
        return ticketRepository.buyTicket(Long.parseLong(eventId), Long.parseLong(userId), categoryName, purchaseType);
    }

}
