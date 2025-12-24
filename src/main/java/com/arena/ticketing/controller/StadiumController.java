package com.arena.ticketing.controller;

import com.arena.ticketing.dto.StadiumDTO;
import com.arena.ticketing.service.StadiumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stadiums")
public class StadiumController {

    private final StadiumService stadiumService;

    // Injectăm serviciul prin constructor
    public StadiumController(StadiumService stadiumService) {
        this.stadiumService = stadiumService;
    }

    // Endpoint pentru a vedea toate stadioanele
    @GetMapping
    public ResponseEntity<List<StadiumDTO>> getAllStadiums() {
        return ResponseEntity.ok(stadiumService.getAllStadiums());
    }

    // Endpoint pentru a crea un stadion nou
    @PostMapping
    public ResponseEntity<StadiumDTO> createStadium(@RequestBody StadiumDTO stadiumDTO) {
        return ResponseEntity.ok(stadiumService.createStadium(stadiumDTO));
    }
}