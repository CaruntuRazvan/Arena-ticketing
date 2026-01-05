package com.arena.ticketing.controller;

import com.arena.ticketing.dto.TicketRequestDTO;
import com.arena.ticketing.model.Ticket;
import com.arena.ticketing.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/buy")
    public ResponseEntity<Ticket> buyTicket(@RequestBody TicketRequestDTO request) {
        Ticket bilet = ticketService.buyTicket(request);
        return ResponseEntity.ok(bilet);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getMyTickets(@PathVariable Long userId) {
        // Corect: Controller -> Service -> Repository
        return ResponseEntity.ok(ticketService.getTicketsByUserId(userId));
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<Ticket>> getTicketsByMatch(@PathVariable Long matchId) {
        return ResponseEntity.ok(ticketService.getTicketsByMatch(matchId));
    }

    @GetMapping("/revenue/match/{matchId}")
    public ResponseEntity<Double> getMatchRevenue(@PathVariable Long matchId) {
        return ResponseEntity.ok(ticketService.getTotalRevenueByMatch(matchId));
    }

}