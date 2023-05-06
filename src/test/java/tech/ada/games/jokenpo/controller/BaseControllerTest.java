package tech.ada.games.jokenpo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tech.ada.games.jokenpo.dto.LoginDto;
import tech.ada.games.jokenpo.dto.PlayerDto;
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
		if (playerRepository.findByUsername(username).isEmpty()) {

			PlayerDto playerDto = new PlayerDto(username, password, username);
			mockMvc
				.perform(post("/api/v1/jokenpo/player/create")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(playerDto)))
				.andExpect(status().isCreated());
		}
	}
	
	protected void login() throws JsonProcessingException, Exception {
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
	
	protected String getAuthorization() {
		return "Bearer " + this.token;
	}
}
