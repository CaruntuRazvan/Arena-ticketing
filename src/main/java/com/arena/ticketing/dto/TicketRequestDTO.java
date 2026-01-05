package com.arena.ticketing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TicketRequestDTO {
    private Long matchId;
    private Long seatId;
    private Long userId;
}