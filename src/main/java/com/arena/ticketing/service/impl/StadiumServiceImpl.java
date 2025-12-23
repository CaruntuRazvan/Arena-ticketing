package com.arena.ticketing.service.impl;

import com.arena.ticketing.dto.StadiumDTO;
import com.arena.ticketing.model.Stadium;
import com.arena.ticketing.repository.StadiumRepository;
import com.arena.ticketing.service.StadiumService;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;

    public StadiumServiceImpl(StadiumRepository stadiumRepository) {
        this.stadiumRepository = stadiumRepository;
    }

    @Override
    public StadiumDTO createStadium(StadiumDTO dto) {
        Stadium stadium = new Stadium();
        stadium.setName(dto.getName());
        stadium.setLocation(dto.getLocation());

        Stadium saved = stadiumRepository.save(stadium);
        dto.setId(saved.getId());
        return dto;
    }

    @Override
    public List<StadiumDTO> getAllStadiums() {
        return stadiumRepository.findAll().stream().map(s -> {
            StadiumDTO dto = new StadiumDTO();
            dto.setId(s.getId());
            dto.setName(s.getName());
            dto.setLocation(s.getLocation());
            dto.setNumberOfSectors(s.getSectors() != null ? s.getSectors().size() : 0);
            return dto;
        }).collect(Collectors.toList());
    }
}