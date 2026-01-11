package com.arena.ticketing.service;

import com.arena.ticketing.dto.TicketRequestDTO;
import com.arena.ticketing.exception.TicketException;
import com.arena.ticketing.model.Ticket;
import com.arena.ticketing.repository.MatchRepository;
import com.arena.ticketing.repository.SeatRepository;
import com.arena.ticketing.repository.TicketRepository;
import com.arena.ticketing.repository.UserRepository;
import com.arena.ticketing.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void buyTickets_ShouldThrowException_WhenSeatIsAlreadyTaken() {
        // GIVEN
        // Trimitem o listă de ID-uri [10L], nu un singur Long
        TicketRequestDTO request = new TicketRequestDTO(1L, List.of(10L),1L );

        // Simulăm că locul 10 este deja ocupat
        // Atenție: Metoda din service verifică acum existența meciului/userului înainte de scaun
        when(matchRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(true);
        // Dacă în TicketServiceImpl ai folosit findById în loc de existsById, va trebui să faci mock la findById(Optional.of(match))

        when(ticketRepository.existsByMatchIdAndSeatId(1L, 10L)).thenReturn(true);

        // WHEN & THEN
        TicketException exception = assertThrows(TicketException.class, () -> {
            ticketService.buyTickets(request); // Metoda plurală
        });

        assertEquals("Locul cu ID-ul 10 este deja ocupat!", exception.getMessage());

        verify(ticketRepository, never()).save(any());
    }
}