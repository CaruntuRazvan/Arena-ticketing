package com.arena.ticketing.dto;

import lombok.Data;

@Data
public class PriceRequestDTO {
    private Long matchId;
    private Long sectorId;
    private Double price;
}
