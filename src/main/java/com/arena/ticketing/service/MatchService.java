package com.arena.ticketing.service;

import com.arena.ticketing.model.Match;
import com.arena.ticketing.dto.MatchRequestDTO;
import com.arena.ticketing.dto.PriceRequestDTO;
import java.util.List;

public interface MatchService {
    Match createMatch(MatchRequestDTO dto);
    void setMatchPrices(List<PriceRequestDTO> prices);
    Match saveMatch(Match match);
    List<Match> getAllMatches();
    Double getPriceForSector(Long matchId, Long sectorId);
    List<Match> getUpcomingMatches();
}