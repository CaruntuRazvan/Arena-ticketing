package com.arena.ticketing.service.impl;

import com.arena.ticketing.dto.TicketRequestDTO;
import com.arena.ticketing.exception.TicketException;
import com.arena.ticketing.model.*;
import com.arena.ticketing.repository.*;
import com.arena.ticketing.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final MatchRepository matchRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final MatchSectorPriceRepository priceRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<Ticket> buyTickets(TicketRequestDTO request) {
        // 1. Validare cantitate (Regulă nouă)
        if (request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            throw new TicketException("Trebuie să selectați cel puțin un loc!");
        }
        if (request.getSeatIds().size() > 5) {
            throw new TicketException("Puteți cumpăra maxim 5 bilete per tranzacție!");
        }

        // 2. Validăm entitățile generale (o singură dată)
        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new TicketException("Meciul nu a fost găsit!"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new TicketException("Utilizatorul nu a fost găsit!"));

        // Validări status meci (rămân la fel)
        if (match.getStatus() == MatchStatus.CANCELLED) throw new TicketException("Meciul a fost anulat!");
        if (match.getStatus() == MatchStatus.FINISHED) throw new TicketException("Meciul s-a terminat!");
        if (match.getMatchDate().isBefore(LocalDateTime.now())) throw new TicketException("Meciul a trecut deja!");

        List<Ticket> savedTickets = new ArrayList<>();

        // 3. Procesăm fiecare loc din listă
        for (Long seatId : request.getSeatIds()) {

            // Verificăm dacă locul e ocupat
            if (ticketRepository.existsByMatchIdAndSeatId(match.getId(), seatId)) {
                throw new TicketException("Locul cu ID-ul " + seatId + " este deja ocupat!");
            }

            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new TicketException("Locul " + seatId + " nu a fost găsit!"));

            // Căutăm prețul pentru sectorul acestui scaun
            MatchSectorPrice priceConfig = priceRepository.findByMatchIdAndSectorId(
                    match.getId(),
                    seat.getSector().getId()
            ).orElseThrow(() -> new TicketException("Prețul pentru sectorul " + seat.getSector().getName() + " nu este configurat!"));

            // 4. Creăm biletul
            Ticket ticket = new Ticket();
            ticket.setMatch(match);
            ticket.setSeat(seat);
            ticket.setUser(user);
            ticket.setFinalPrice(priceConfig.getPrice());
            ticket.setPurchaseDate(LocalDateTime.now());

            savedTickets.add(ticketRepository.save(ticket));
        }

        return savedTickets;
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    @Override
    public List<Ticket> getTicketsByMatch(Long matchId) {
        // Verificăm dacă meciul există (opțional, dar recomandat)
        if (!matchRepository.existsById(matchId)) {
            throw new TicketException("Meciul cu ID-ul " + matchId + " nu există.");
        }
        return ticketRepository.findByMatchId(matchId);
    }
    @Override
    public List<Ticket> getTicketsByUserId(Long userId) {
        // Aici am putea adăuga logică: ex. să verificăm dacă user-ul există
        if (!userRepository.existsById(userId)) {
            throw new TicketException("Utilizatorul nu a fost găsit");
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

    @Override
    @Transactional
    public void validateTicket(String ticketCode) {
        // Căutăm biletul după codul UUID, nu după ID-ul bazei de date
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new TicketException("Cod invalid! Biletul nu există în sistem."));

        if (ticket.isUsed()) {
            throw new TicketException("Acces refuzat! Acest bilet a fost deja scanat.");
        }

        // Verificăm dacă meciul este în desfășurare sau urmează (nu în trecut)
        if (ticket.getMatch().getMatchDate().isBefore(java.time.LocalDateTime.now().minusHours(3))) {
            throw new TicketException("Acces refuzat! Acest bilet este pentru un eveniment care a trecut.");
        }

        ticket.setUsed(true);
        ticketRepository.save(ticket);
    }
}