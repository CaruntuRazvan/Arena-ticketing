package com.arena.ticketing.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MatchRequestDTO {
    private String opponentName;
    private LocalDateTime matchDate;
    private Long stadiumId;
}