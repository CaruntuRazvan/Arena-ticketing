package com.arena.ticketing.controller;

import com.arena.ticketing.model.Match;
import com.arena.ticketing.model.MatchStatus;
import com.arena.ticketing.service.MatchService;
import com.arena.ticketing.model.MatchSectorPrice;
import com.arena.ticketing.dto.MatchRequestDTO;
import com.arena.ticketing.dto.PriceRequestDTO;
import com.arena.ticketing.dto.MatchStatsDTO;
import com.arena.ticketing.dto.SectorAvailabilityDTO;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Match>> getUpcomingMatches() {
        return ResponseEntity.ok(matchService.getUpcomingMatches());
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@Valid @RequestBody MatchRequestDTO dto) {
        return ResponseEntity.ok(matchService.createMatch(dto));
    }

    @PostMapping("/prices")
    public ResponseEntity<String> setPrices(@Valid @RequestBody List<PriceRequestDTO> prices) {
        matchService.setMatchPrices(prices);
        return ResponseEntity.ok("Prețurile au fost setate cu succes!");
    }

    @GetMapping("/{id}/statistics")
    @Operation(summary = "Statistici ocupare meci", description = "Returnează procentul de ocupare total și pe fiecare sector în parte.")
    public ResponseEntity<MatchStatsDTO> getMatchStats(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatchStatistics(id));
    }

    @GetMapping("/{matchId}/sectors-availability")
    public ResponseEntity<List<SectorAvailabilityDTO>> getSectorsAvailability(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.getSectorsAvailabilityForMatch(matchId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Schimbă statusul unui meci", description = "Admin-ul poate anula sau finaliza un meci.")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long id,
            @RequestParam MatchStatus status) {

        matchService.updateMatchStatus(id, status);
        return ResponseEntity.ok("Statusul meciului a fost actualizat în " + status);
    }
}