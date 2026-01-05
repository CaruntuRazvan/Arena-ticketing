package com.arena.ticketing.controller;

import com.arena.ticketing.dto.StadiumDTO;
import com.arena.ticketing.service.StadiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.arena.ticketing.dto.SectorRequestDTO;
import com.arena.ticketing.model.Sector;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/stadiums")
@RequiredArgsConstructor
public class StadiumController {

    private final StadiumService stadiumService;

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
    @PostMapping("/sectors")
    public ResponseEntity<Sector> addSector(@RequestBody SectorRequestDTO dto) {
        return ResponseEntity.ok(stadiumService.addSector(dto));
    }
}