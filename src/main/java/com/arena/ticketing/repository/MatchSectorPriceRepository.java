package com.arena.ticketing.repository;
import com.arena.ticketing.model.MatchSectorPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MatchSectorPriceRepository extends JpaRepository<MatchSectorPrice, Long> {
}