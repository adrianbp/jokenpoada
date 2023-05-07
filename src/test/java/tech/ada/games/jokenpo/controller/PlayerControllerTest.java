package tech.ada.games.jokenpo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.model.Role;
import tech.ada.games.jokenpo.service.PlayerService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws JsonProcessingException, Exception {
        this.createUserIfNotExists();
        this.login();
    }


    @Test
    public void testCreatePlayer() throws Exception {

         String username = "usuario1";
         String password = "senha1";
         String token = null;

        PlayerDto playerDto = new PlayerDto(username, password, token);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/jokenpo/player/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testFindPlayers() throws Exception {
;
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("acess_1");
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        Player player1 = new Player(1L, "John1", "12232", "John", roles);


        Player player2 = new Player(1L, "Jane1", "12232", "Jane", roles);
        List<Player> players = Arrays.asList(player1, player2);

        Mockito.when(playerService.findPlayers()).thenReturn(players);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/jokenpo/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.getAuthorization())
                )

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("John1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("Jane1"));
    }

    @Test
    public void testFindPlayer() throws Exception {
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("acess_1");
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        Player player = new Player(1L, "John1", "12232", "John", roles);

        Mockito.when(playerService.findByPlayer(Mockito.anyString())).thenReturn(player);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/jokenpo/player/{player}", "John")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.getAuthorization())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("John1"));
    }

    @Test
    public void testDeletePlayer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/jokenpo/player/{player}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.getAuthorization())
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


}
