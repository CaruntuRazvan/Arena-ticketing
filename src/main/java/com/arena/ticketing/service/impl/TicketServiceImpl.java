package com.arena.ticketing.service.impl;

import com.arena.ticketing.dto.TicketRequestDTO;
import com.arena.ticketing.model.*;
import com.arena.ticketing.repository.*;
import com.arena.ticketing.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final MatchRepository matchRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final MatchSectorPriceRepository priceRepository;

    @Override
    @Transactional
    public Ticket buyTicket(TicketRequestDTO request) {

        // 1. Verificăm dacă locul este deja ocupat pentru acest meci (Regulă de Business)
        if (ticketRepository.existsByMatchIdAndSeatId(request.getMatchId(), request.getSeatId())) {
            throw new RuntimeException("Locul este deja rezervat pentru acest meci!");
        }

        // 2. Validăm existența entităților în baza de date
        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new RuntimeException("Meciul nu a fost găsit!"));

        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Locul nu a fost găsit!"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit!"));

        if (match.getStatus() == MatchStatus.CANCELLED) {
            throw new RuntimeException("Nu se pot cumpăra bilete: Meciul a fost anulat!");
        }

        if (match.getStatus() == MatchStatus.FINISHED) {
            throw new RuntimeException("Nu se pot cumpăra bilete: Meciul s-a terminat deja!");
        }

        if (match.getMatchDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Nu se mai pot cumpăra bilete pentru un meci care a avut loc deja!");
        }
        // 3. Calculăm prețul final (Căutăm prețul setat pentru acest meci și acest sector)
        MatchSectorPrice priceConfig = priceRepository.findByMatchIdAndSectorId(
                match.getId(),
                seat.getSector().getId()
        ).orElseThrow(() -> new RuntimeException("Prețul pentru acest sector nu a fost configurat!"));

        // 4. Creăm și populăm obiectul Ticket
        Ticket ticket = new Ticket();
        ticket.setMatch(match);
        ticket.setSeat(seat);
        ticket.setUser(user);
        ticket.setFinalPrice(priceConfig.getPrice());
        ticket.setPurchaseDate(LocalDateTime.now()); // Asigură-te că ai acest câmp în clasa Ticket

        // 5. Salvăm biletul
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    @Override
    public List<Ticket> getTicketsByMatch(Long matchId) {
        // Verificăm dacă meciul există (opțional, dar recomandat)
        if (!matchRepository.existsById(matchId)) {
            throw new RuntimeException("Meciul cu ID-ul " + matchId + " nu există.");
        }
        return ticketRepository.findByMatchId(matchId);
    }
    @Override
    public List<Ticket> getTicketsByUserId(Long userId) {
        // Aici am putea adăuga logică: ex. să verificăm dacă user-ul există
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Utilizatorul nu a fost găsit");
        }
        return ticketRepository.findByUserId(userId);
    }

    @Override
    public Double getTotalRevenueByMatch(Long matchId) {
        List<Ticket> matchTickets = ticketRepository.findByMatchId(matchId);
        return matchTickets.stream()
                .mapToDouble(Ticket::getFinalPrice)
                .sum();
    }
}