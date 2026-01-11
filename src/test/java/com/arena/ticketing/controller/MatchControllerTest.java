package com.arena.ticketing.controller;

import com.arena.ticketing.controller.MatchController;
import com.arena.ticketing.dto.MatchStatsDTO;
import com.arena.ticketing.model.Match;
import com.arena.ticketing.service.MatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;

    @Test
    void testGetAllMatches_Endpoint() throws Exception {
        // GIVEN
        Match match = new Match();
        match.setOpponentName("Belgia");
        when(matchService.getAllMatches()).thenReturn(List.of(match));

        // WHEN & THEN
        mockMvc.perform(get("/api/matches"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].opponentName").value("Belgia"));
    }

    @Test
    void testGetMatchStats_Endpoint() throws Exception {
        // GIVEN
        Long matchId = 1L;
        MatchStatsDTO stats = new MatchStatsDTO(
                matchId,
                "Echipa Oaspete",
                1000L,
                500L,
                50.0,
                new HashMap<>()
        );

        when(matchService.getMatchStatistics(matchId)).thenReturn(stats);

        // WHEN & THEN
        mockMvc.perform(get("/api/matches/{id}/statistics", matchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchId").value(matchId))
                .andExpect(jsonPath("$.opponentName").value("Echipa Oaspete"))
                .andExpect(jsonPath("$.ticketsSold").value(500))
                .andExpect(jsonPath("$.overallOccupancyPercentage").value(50.0));
    }

    @Test
    void testGetSectorsAvailability_Endpoint() throws Exception {
        // WHEN & THEN
        mockMvc.perform(get("/api/matches/1/sectors-availability"))
                .andExpect(status().isOk());
    }
}