package com.arena.ticketing.dto;

import lombok.Data;

@Data
public class SectorRequestDTO {
    private String name;
    private int rows;
    private int seatsPerRow;
    private Long stadiumId;
}