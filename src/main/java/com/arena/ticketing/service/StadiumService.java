package com.arena.ticketing.service;

import com.arena.ticketing.dto.StadiumDTO;
import java.util.List;

public interface StadiumService {
    StadiumDTO createStadium(StadiumDTO stadiumDTO);
    List<StadiumDTO> getAllStadiums();
}