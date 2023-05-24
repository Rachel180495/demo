package com.example.demo;

import com.example.demo.model.Player;
import com.example.demo.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerRepository playerRepository;

    @Test
    public void testGetAllUsers() throws Exception {
        // Arrange
        List<Player> players = new ArrayList<>();
        Player player1 = new Player();
        player1.setPlayerID("kkk");
        player1.setNameFirst("mmm");
        Player player2 = new Player();
        player2.setPlayerID("bbb");
        player2.setNameFirst("ooo");

        players.add(player1);
        players.add(player2);

        Mockito.when(playerRepository.findAll()).thenReturn(players);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/players")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].playerID").value("kkk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nameFirst").value("mmm"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].playerID").value("bbb"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nameFirst").value("ooo"));
    }

    @Test
    public void testGetUserById() throws Exception {
        // Arrange
        String playerId = "kjkj";
        Player player = new Player();
        player.setPlayerID(playerId);
        player.setNameFirst("John");
        Mockito.when(playerRepository.findByPlayerID(playerId)).thenReturn(Optional.of(player));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/players/{id}", playerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.playerID").value(playerId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nameFirst").value("John"));
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        // Arrange
        Long playerId = 1L;

        Mockito.when(playerRepository.findByPlayerID("kk")).thenReturn(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/players/{id}", playerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}