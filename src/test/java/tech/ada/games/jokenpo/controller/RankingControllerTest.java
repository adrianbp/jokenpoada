package tech.ada.games.jokenpo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.dto.RankingDto;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.Game;
import tech.ada.games.jokenpo.model.GameWinner;
import tech.ada.games.jokenpo.model.GameWinnerId;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.repository.GameRepository;
import tech.ada.games.jokenpo.repository.GameWinnerRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class RankingControllerTest extends BaseControllerTest {

	@Autowired
	private GameWinnerRepository gameWinnerRepository;
	
	@Autowired
	private GameRepository gameRepository;
	
	@BeforeEach
	public void setUp() throws JsonProcessingException, Exception {
		this.createUserIfNotExists();
		this.login();
	}
	
	@AfterEach
	public void cleanUp() {
		this.gameWinnerRepository.deleteAll();
		this.gameRepository.deleteAll();
		this.playerRepository.deleteAll();
	}
	
	@Test
	public void testRanking() throws Exception {
		// Arrange
		Player loggedPlayer = playerRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("Usuario não encontrado"));
		PlayerDto opponentDto = new PlayerDto("opponent", "pass", "Opponent");
		mockMvc
			.perform(post("/api/v1/jokenpo/player/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(opponentDto)))
			.andExpect(status().isCreated());
		Player opponent = playerRepository.findByUsername("opponent").orElseThrow(() -> new DataNotFoundException("Usuario oponente não encontrado"));
		
		Game game1 = gameRepository.save(new Game());
		Game game2 = gameRepository.save(new Game());
		Game game3 = gameRepository.save(new Game());
		
		GameWinner gameWinner1 = new GameWinner(new GameWinnerId(game1.getId(), loggedPlayer.getId()), loggedPlayer);
		GameWinner gameWinner2 = new GameWinner(new GameWinnerId(game2.getId(), opponent.getId()), opponent);
		GameWinner gameWinner3 = new GameWinner(new GameWinnerId(game3.getId(), loggedPlayer.getId()), loggedPlayer);
		gameWinnerRepository.save(gameWinner1);
		gameWinnerRepository.save(gameWinner2);
		gameWinnerRepository.save(gameWinner3);
		
		// Act
	 	MvcResult result = mockMvc.perform(get("/api/v1/jokenpo/ranking")
						.header("Authorization", this.getAuthorization()))
			.andExpect(status().isOk())
			.andReturn();
		
		// Assert
		List<RankingDto> rankings = objectMapper.readValue(
				result.getResponse().getContentAsString(),
				new TypeReference<List<RankingDto>>() {});
		assertEquals(2, rankings.size());
		assertEquals(new RankingDto(1, new PlayerDto(loggedPlayer), 2L), rankings.get(0));
		assertEquals(new RankingDto(2, new PlayerDto(opponent), 1L), rankings.get(1));
	}
}
