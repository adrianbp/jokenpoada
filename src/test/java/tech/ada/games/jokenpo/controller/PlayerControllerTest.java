package tech.ada.games.jokenpo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.ada.games.jokenpo.dto.LoginDto;
import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.model.Role;
import tech.ada.games.jokenpo.response.AuthResponse;
import tech.ada.games.jokenpo.service.AuthService;
import tech.ada.games.jokenpo.service.PlayerService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTest extends BaseControllerTest {



    @BeforeEach
    public void setUp() throws JsonProcessingException, Exception {
        this.createUserIfNotExists();
        this.login();
    }
    

    @Test
    public void testCreatePlayer() throws Exception {

         String username = "usuario2";
         String password = "senha1";
         String token = "usuario2";

        PlayerDto playerDto = new PlayerDto(username, password, token);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/jokenpo/player/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    public void testFindPlayers() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/jokenpo/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.getAuthorization())
                )

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("usuario1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("usuario1"));

    }

    @Test
    public void testFindPlayer() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/jokenpo/player/{player}", "usuario1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.getAuthorization())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("usuario1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("usuario1"));
    }

    @Test
    public void testDeletePlayer() throws Exception {
    	Long userId = playerRepository.findByUsername(username).get().getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/jokenpo/player/{player}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.getAuthorization())
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


}
