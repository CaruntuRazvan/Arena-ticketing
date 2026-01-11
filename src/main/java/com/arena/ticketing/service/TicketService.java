package com.arena.ticketing.service;

import com.arena.ticketing.dto.TicketRequestDTO;
import com.arena.ticketing.model.Ticket;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TicketService {
    // Aceasta este metoda principală pe care o cere proiectul (Business Logic)

    @Transactional(isolation = Isolation.SERIALIZABLE)
    List<Ticket> buyTickets(TicketRequestDTO request);

    List<Ticket> getAllTickets();
    List<Ticket> getTicketsByMatch(Long matchId);
    List<Ticket> getTicketsByUserId(Long userId);
    Double getTotalRevenueByMatch(Long matchId);

    void validateTicket(String ticketCode);
}