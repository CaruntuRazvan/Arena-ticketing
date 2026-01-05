package com.arena.ticketing.service.impl;

import com.arena.ticketing.model.Seat;
import com.arena.ticketing.repository.SeatRepository;
import com.arena.ticketing.repository.TicketRepository;
import com.arena.ticketing.service.SeatService;
import com.arena.ticketing.dto.SeatStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository; // Adăugăm repository-ul pentru bilete

    @Override
    public List<Seat> getSeatsBySector(Long sectorId) {
        return seatRepository.findBySectorId(sectorId);
    }

    @Override
    public List<SeatStatusDTO> getSeatsStatusByMatch(Long matchId, Long sectorId) {
        List<Seat> allSeats = seatRepository.findBySectorId(sectorId);

        return allSeats.stream().map(seat -> {
            // Verificăm dacă există bilet pentru acest meci și acest loc
            boolean isTaken = ticketRepository.existsByMatchIdAndSeatId(matchId, seat.getId());

            return new SeatStatusDTO(
                    seat.getId(),
                    seat.getRowNumber(),
                    seat.getSeatNumber(),
                    !isTaken // Este disponibil dacă NU este luat
            );
        }).collect(Collectors.toList());
    }
}