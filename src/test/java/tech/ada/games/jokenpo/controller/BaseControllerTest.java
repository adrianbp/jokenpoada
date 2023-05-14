package tech.ada.games.jokenpo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import tech.ada.games.jokenpo.dto.LoginDto;
import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.repository.PlayerRepository;
import tech.ada.games.jokenpo.response.AuthResponse;

public class BaseControllerTest {
	protected String username = "usuario1";
	protected String password = "senha1";
	protected String token = null;

	@Autowired
	protected PlayerRepository playerRepository;
	
	@Autowired
	protected MockMvc mockMvc;
	
	@Autowired
	protected ObjectMapper objectMapper;
	
	protected void createUserIfNotExists() throws JsonProcessingException, Exception {
		PlayerDto playerDto = new PlayerDto(username, password, username);
		createUserIfNotExists(playerDto);
	}
	
	protected void createUserIfNotExists(PlayerDto playerDto) throws JsonProcessingException, Exception {
		System.out.println("Verificando usuário: " + playerDto.getUsername());
		if (playerRepository.findByUsername(playerDto.getUsername()).isEmpty()) {

			System.out.println("Criando usuário: " + playerDto.getUsername());
			mockMvc
				.perform(post("/api/v1/jokenpo/player/create")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(playerDto)))
				.andExpect(status().isCreated());
			System.out.println("Criou usuário: " + playerDto.getUsername());
		}
		else {
			System.out.println("Usuário já existe: " + playerDto.getUsername());
		}
	}
	
	protected void login() throws JsonProcessingException, Exception {
		System.out.println("Login 1: " + username);
		LoginDto loginDto = new LoginDto(username, password);

		MvcResult loginResult = mockMvc
			.perform(post("/api/v1/jokenpo/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(loginDto)))
			.andExpect(status().isOk())
			.andReturn();
		
		AuthResponse authResponse = objectMapper.readValue(
				loginResult.getResponse().getContentAsString(),
				AuthResponse.class);
		
		this.token = authResponse.getAccessToken();
	}
	
	/*
	protected void login() throws JsonProcessingException, Exception {		
		this.token = this.login(username, password);
	}*/
	
	protected String login(String user, String pass) throws JsonProcessingException, Exception {
		System.out.println("Login 2: " + user);
		LoginDto loginDto = new LoginDto(user, pass);

		MvcResult loginResult = mockMvc
			.perform(post("/api/v1/jokenpo/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(loginDto)))
			.andExpect(status().isOk())
			.andReturn();
		
		AuthResponse authResponse = objectMapper.readValue(
				loginResult.getResponse().getContentAsString(),
				AuthResponse.class);
		
		return authResponse.getAccessToken();
	}
	
	public Player getPlayer(String username) throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/jokenpo/player/" + username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", this.getAuthorization())
                )

        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
        
        return objectMapper.readValue(
				result.getResponse().getContentAsString(),
				new TypeReference<Player>() {});
	}
	
	protected String getAuthorization() {
		return "Bearer " + this.token;
	}
}
