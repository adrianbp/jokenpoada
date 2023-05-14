package tech.ada.games.jokenpo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.model.Role;
import tech.ada.games.jokenpo.repository.RoleRepository;


import java.util.HashSet;
import java.util.Set;




@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTest extends BaseControllerTest {

    @Autowired
    private RoleRepository roleRepository;


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
    @Transactional
    public void testDeletePlayer() throws Exception {

        Player player = new Player();
        player.setUsername("usuario");
        player.setName("Joao");
        player.setPassword("BLALOC");

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_USER").get();
        roles.add(role);
        player.setRoles(roles);
        Player p = playerRepository.save(player);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/jokenpo/player/{player}", p.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.getAuthorization())
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


}
