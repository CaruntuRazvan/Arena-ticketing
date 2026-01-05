package com.arena.ticketing.controller;

import com.arena.ticketing.model.Match;
import com.arena.ticketing.service.MatchService;
import com.arena.ticketing.model.MatchSectorPrice;
import com.arena.ticketing.dto.MatchRequestDTO;
import com.arena.ticketing.dto.PriceRequestDTO;
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
    public ResponseEntity<Match> createMatch(@RequestBody MatchRequestDTO dto) {
        return ResponseEntity.ok(matchService.createMatch(dto));
    }

    @PostMapping("/prices")
    public ResponseEntity<String> setPrices(@RequestBody List<PriceRequestDTO> prices) {
        matchService.setMatchPrices(prices);
        return ResponseEntity.ok("Prețurile au fost setate cu succes!");
    }
}