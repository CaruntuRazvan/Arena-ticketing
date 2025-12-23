package com.arena.ticketing.service;

import com.arena.ticketing.model.Match;
import java.util.List;

public interface MatchService {
    Match saveMatch(Match match);
    List<Match> getAllMatches();
    Double getPriceForSector(Long matchId, Long sectorId);
}