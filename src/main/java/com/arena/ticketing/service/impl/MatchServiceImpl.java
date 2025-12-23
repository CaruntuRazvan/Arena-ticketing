package com.arena.ticketing.service.impl;

import com.arena.ticketing.model.Match;
import com.arena.ticketing.model.MatchSectorPrice;
import com.arena.ticketing.repository.MatchRepository;
import com.arena.ticketing.repository.MatchSectorPriceRepository;
import com.arena.ticketing.service.MatchService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchSectorPriceRepository priceRepository;

    // Injection prin constructor (Standard Java Best Practice)
    public MatchServiceImpl(MatchRepository matchRepository, MatchSectorPriceRepository priceRepository) {
        this.matchRepository = matchRepository;
        this.priceRepository = priceRepository;
    }

    @Override
    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Double getPriceForSector(Long matchId, Long sectorId) {
        return priceRepository.findAll().stream()
                .filter(p -> p.getMatch().getId().equals(matchId) && p.getSector().getId().equals(sectorId))
                .map(MatchSectorPrice::getPrice)
                .findFirst()
                .orElse(0.0);
    }
}