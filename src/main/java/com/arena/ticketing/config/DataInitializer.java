package com.arena.ticketing.config;

import com.arena.ticketing.model.*;
import com.arena.ticketing.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            StadiumRepository stadiumRepository,
            SectorRepository sectorRepository,
            SeatRepository seatRepository,
            MatchRepository matchRepository,
            MatchSectorPriceRepository priceRepository,
            UserRepository userRepository) { // Am adaugat UserRepository aici
        return args -> {
            if (stadiumRepository.count() == 0) {
                // 1. Creăm Stadionul
                Stadium stadium = new Stadium();
                stadium.setName("Arena Nationala");
                stadium.setLocation("Bucuresti");
                stadium = stadiumRepository.save(stadium);

                // 2. Creăm un User de test (Necesar pentru a asocia biletul la un cumparator)
                User user = new User();
                user.setUsername("andrei_test");
                user.setEmail("andrei@test.com");
                userRepository.save(user);

                // 3. Creăm Meciul
                Match match = new Match();
                match.setOpponentName("Germania");
                match.setMatchDate(LocalDateTime.now().plusDays(10));
                match.setStadium(stadium);
                match = matchRepository.save(match);

                // 4. Definim Sectoarele si generam Locurile automat
                String[] sectorNames = {"Tribuna 1", "Tribuna 2", "Peluza Nord", "Peluza Sud"};
                Double[] basePrices = {200.0, 150.0, 50.0, 50.0};

                for (int i = 0; i < sectorNames.length; i++) {
                    // Salvare Sector
                    Sector sector = new Sector();
                    sector.setName(sectorNames[i]);
                    sector.setStadium(stadium);
                    sector = sectorRepository.save(sector);

                    // Setare Pret pentru acest sector la meciul curent
                    MatchSectorPrice msp = new MatchSectorPrice();
                    msp.setMatch(match);
                    msp.setSector(sector);
                    msp.setPrice(basePrices[i]);
                    priceRepository.save(msp);

                    // Generare automată locuri: 3 rânduri x 10 locuri = 30 locuri per sector
                    for (int r = 1; r <= 3; r++) {
                        for (int l = 1; l <= 10; l++) {
                            Seat seat = new Seat();
                            seat.setRowNumber(r);
                            seat.setSeatNumber(l);
                            seat.setSector(sector);
                            seatRepository.save(seat);
                        }
                    }
                }

                System.out.println(">>> Baza de date a fost populata: 1 Stadion, 1 User, 1 Meci, 4 Sectoare si 120 de Locuri!");
            }
        };
    }
}