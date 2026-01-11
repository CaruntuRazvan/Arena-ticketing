package com.arena.ticketing.controller;

import com.arena.ticketing.dto.TicketRequestDTO;
import com.arena.ticketing.model.Ticket;
import com.arena.ticketing.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/buy")
    public ResponseEntity<List<Ticket>> buyTicket(@Valid @RequestBody TicketRequestDTO request) {
        // Apelăm metoda buyTickets (cu "s" la final) care returnează o listă
        List<Ticket> bilete = ticketService.buyTickets(request);
        return ResponseEntity.ok(bilete);
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

    @PatchMapping("/validate/{ticketCode}")
    @Operation(summary = "Scanare bilet la intrare", description = "Validează biletul folosind codul unic UUID.")
    public ResponseEntity<String> validateTicket(@PathVariable String ticketCode) {
        ticketService.validateTicket(ticketCode);
        return ResponseEntity.ok("Acces permis! Biletul " + ticketCode + " a fost validat.");
    }

}