package com.arena.ticketing.controller;

import com.arena.ticketing.model.Seat;
import com.arena.ticketing.service.SeatService;
import com.arena.ticketing.dto.SeatStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService; // Injectam Service-ul

    @GetMapping("/sector/{sectorId}")
    public ResponseEntity<List<Seat>> getSeatsBySector(@PathVariable Long sectorId) {
        return ResponseEntity.ok(seatService.getSeatsBySector(sectorId));
    }

    @GetMapping("/availability")
    public ResponseEntity<List<SeatStatusDTO>> getAvailability(
            @RequestParam Long matchId,
            @RequestParam Long sectorId) {
        return ResponseEntity.ok(seatService.getSeatsStatusByMatch(matchId, sectorId));
    }
}