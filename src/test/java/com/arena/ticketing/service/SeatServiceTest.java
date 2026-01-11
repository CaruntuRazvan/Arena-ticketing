package com.arena.ticketing.service;

import com.arena.ticketing.dto.SeatStatusDTO;
import com.arena.ticketing.model.MatchSectorPrice;
import com.arena.ticketing.model.Seat;
import com.arena.ticketing.repository.MatchRepository;
import com.arena.ticketing.repository.MatchSectorPriceRepository;
import com.arena.ticketing.repository.SeatRepository;
import com.arena.ticketing.repository.TicketRepository;
import com.arena.ticketing.service.impl.SeatServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private MatchSectorPriceRepository matchSectorPriceRepository;

    @InjectMocks
    private SeatServiceImpl seatService;

    @Test
    void testGetSeatsStatus_Success() {
        // 1. Pregătim datele (Mock)
        Long matchId = 1L;
        Long sectorId = 1L;

        Seat seat = new Seat();
        seat.setId(10L);
        seat.setRowNumber(5);
        seat.setSeatNumber(12);

        MatchSectorPrice msp = new MatchSectorPrice();
        msp.setPrice(100.0);

        // 2. Definim comportamentul mock-urilor
        when(matchRepository.existsById(matchId)).thenReturn(true);
        when(seatRepository.existsBySectorId(sectorId)).thenReturn(true);
        when(seatRepository.findBySectorId(sectorId)).thenReturn(List.of(seat));
        when(matchSectorPriceRepository.findByMatchIdAndSectorId(matchId, sectorId))
                .thenReturn(Optional.of(msp));
        when(ticketRepository.existsByMatchIdAndSeatId(matchId, 10L)).thenReturn(false);

        // 3. Executăm metoda
        List<SeatStatusDTO> result = seatService.getSeatsStatusByMatch(matchId, sectorId);

        // 4. Verificăm rezultatele (Assertions)
        assertEquals(1, result.size());
        assertEquals(100.0, result.get(0).getPrice());
        assertTrue(result.get(0).isAvailable());
    }
}