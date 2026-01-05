package com.arena.ticketing.repository;

import com.arena.ticketing.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Această metodă va returna "true" dacă locul e deja ocupat la meciul respectiv
    boolean existsByMatchIdAndSeatId(Long matchId, Long seatId);
    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByMatchId(Long matchId);
}