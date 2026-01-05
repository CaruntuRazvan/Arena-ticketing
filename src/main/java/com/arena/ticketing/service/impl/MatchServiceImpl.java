package com.arena.ticketing.service.impl;

import com.arena.ticketing.model.*;
import com.arena.ticketing.dto.*;
import com.arena.ticketing.repository.*;
import com.arena.ticketing.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Adaugă importul acesta!

import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchSectorPriceRepository matchSectorPriceRepository;
    private final StadiumRepository stadiumRepository;
    private final SectorRepository sectorRepository;
    /*
    // Constructor manual
    public MatchServiceImpl(MatchRepository matchRepository,
                            MatchSectorPriceRepository matchSectorPriceRepository,
                            StadiumRepository stadiumRepository,
                            SectorRepository sectorRepository) {
        this.matchRepository = matchRepository;
        this.matchSectorPriceRepository = matchSectorPriceRepository;
        this.stadiumRepository = stadiumRepository;
        this.sectorRepository = sectorRepository;
    }*/

    @Override
    public Match createMatch(MatchRequestDTO dto) {
        Stadium stadium = stadiumRepository.findById(dto.getStadiumId())
                .orElseThrow(() -> new RuntimeException("Stadionul nu a fost găsit!"));

        Match match = new Match();
        match.setOpponentName(dto.getOpponentName());
        match.setMatchDate(dto.getMatchDate());
        match.setStadium(stadium);

        return matchRepository.save(match);
    }

    @Override
    @Transactional
    public void setMatchPrices(List<PriceRequestDTO> prices) {
        for (PriceRequestDTO dto : prices) {
            Match match = matchRepository.findById(dto.getMatchId())
                    .orElseThrow(() -> new RuntimeException("Meci negăsit"));
            Sector sector = sectorRepository.findById(dto.getSectorId())
                    .orElseThrow(() -> new RuntimeException("Sector negăsit"));

            // Folosim numele corect: matchSectorPriceRepository
            MatchSectorPrice msp = matchSectorPriceRepository
                    .findByMatchIdAndSectorId(dto.getMatchId(), dto.getSectorId())
                    .orElse(new MatchSectorPrice());

            msp.setMatch(match);
            msp.setSector(sector);
            msp.setPrice(dto.getPrice());

            matchSectorPriceRepository.save(msp);
        }
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

        return matchSectorPriceRepository.findByMatchIdAndSectorId(matchId, sectorId)
                .map(MatchSectorPrice::getPrice)
                .orElse(0.0);
    }

    @Override
    public List<Match> getUpcomingMatches() {
        return matchRepository.findByMatchDateAfterAndStatus(LocalDateTime.now(), MatchStatus.SCHEDULED);
    }

    // în fiecare zi la miezul nopții
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void autoUpdateMatchStatus() {
        // Luăm toate meciurile programate care au data înainte de "acum"
        List<Match> pastMatches = matchRepository.findByMatchDateBeforeAndStatus(
                LocalDateTime.now(),
                MatchStatus.SCHEDULED
        );

        for (Match match : pastMatches) {
            match.setStatus(MatchStatus.FINISHED);
        }

        if (!pastMatches.isEmpty()) {
            matchRepository.saveAll(pastMatches);
            System.out.println("Automatizare: " + pastMatches.size() + " meciuri au fost trecute în status FINISHED.");
        }
    }
}