package com.arena.ticketing.service;

import com.arena.ticketing.dto.MatchStatsDTO;
import com.arena.ticketing.dto.SectorAvailabilityDTO;
import com.arena.ticketing.exception.TicketException;
import com.arena.ticketing.model.Match;
import com.arena.ticketing.model.MatchSectorPrice;
import com.arena.ticketing.model.Sector;
import com.arena.ticketing.model.Stadium;
import com.arena.ticketing.repository.MatchRepository;
import com.arena.ticketing.repository.MatchSectorPriceRepository;
import com.arena.ticketing.repository.SeatRepository;
import com.arena.ticketing.repository.TicketRepository;
import com.arena.ticketing.service.impl.MatchServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private MatchSectorPriceRepository matchSectorPriceRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private MatchServiceImpl matchService;

    @Test
    void testGetMatchStatistics_FullFlow() {
        // 1. GIVEN - Construim structura exact cum o cere MatchServiceImpl
        Long matchId = 1L;
        Long stadiumId = 100L;
        Long sectorId = 50L;

        Sector sector = new Sector();
        sector.setId(sectorId);
        sector.setName("Tribuna Nord");

        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        stadium.setSectors(List.of(sector)); // List.of creează o listă imutabilă, nu e null

        Match match = new Match();
        match.setId(matchId);
        match.setOpponentName("Farul Constanta");
        match.setStadium(stadium);

        // Mock-uim apelurile către Repository
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(seatRepository.countBySectorStadiumId(stadiumId)).thenReturn(1000L);
        when(ticketRepository.countByMatchId(matchId)).thenReturn(250L);

        // Mock pentru loop-ul de sectoare din interiorul metodei
        when(seatRepository.countBySectorId(sectorId)).thenReturn(500L);
        when(ticketRepository.countByMatchIdAndSeatSectorId(matchId, sectorId)).thenReturn(100L);

        // 2. WHEN
        MatchStatsDTO stats = matchService.getMatchStatistics(matchId);

        // 3. THEN
        assertNotNull(stats);
        assertEquals("Farul Constanta", stats.getOpponentName());
        assertEquals(1000, stats.getTotalCapacity());
        assertEquals(250, stats.getTicketsSold());
        assertEquals(25.0, stats.getOverallOccupancyPercentage()); // (250/1000)*100
        assertTrue(stats.getOccupancyPerSector().containsKey("Tribuna Nord"));
        assertEquals(20.0, stats.getOccupancyPerSector().get("Tribuna Nord")); // (100/500)*100
    }

    @Test
    void testGetSectorsAvailability_Success() {
        // GIVEN
        Long matchId = 1L;
        Long sectorId = 50L;

        Sector sector = new Sector();
        sector.setId(sectorId);
        sector.setName("Peluza");

        Stadium stadium = new Stadium();
        stadium.setSectors(List.of(sector));

        Match match = new Match();
        match.setStadium(stadium);

        MatchSectorPrice msp = new MatchSectorPrice();
        msp.setPrice(45.0);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(seatRepository.countBySectorId(sectorId)).thenReturn(100L);
        when(ticketRepository.countByMatchIdAndSeatSectorId(matchId, sectorId)).thenReturn(10L);
        when(matchSectorPriceRepository.findByMatchIdAndSectorId(matchId, sectorId)).thenReturn(Optional.of(msp));

        // WHEN
        List<SectorAvailabilityDTO> result = matchService.getSectorsAvailabilityForMatch(matchId);

        // THEN
        assertEquals(1, result.size());
        assertEquals(90, result.get(0).getAvailableSeats()); // 100 - 10
        assertEquals(45.0, result.get(0).getPrice());
    }
}