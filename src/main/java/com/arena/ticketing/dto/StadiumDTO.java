package com.arena.ticketing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StadiumDTO {
    private Long id;
    private String name;
    private String location;
    private int numberOfSectors; // Informatie calculata, nu exista in tabela Stadium
}