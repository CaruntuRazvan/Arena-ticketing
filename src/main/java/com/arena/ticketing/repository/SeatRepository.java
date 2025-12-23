package com.arena.ticketing.repository;
import com.arena.ticketing.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
}