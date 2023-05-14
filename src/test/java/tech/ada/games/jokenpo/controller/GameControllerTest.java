package tech.ada.games.jokenpo.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import tech.ada.games.jokenpo.dto.GameDto;
import tech.ada.games.jokenpo.dto.GameMoveDto;
import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.dto.ResultDto;
import tech.ada.games.jokenpo.model.Game;
import tech.ada.games.jokenpo.model.Move;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.model.Role;
import tech.ada.games.jokenpo.repository.GameRepository;
import tech.ada.games.jokenpo.repository.MoveRepository;
import tech.ada.games.jokenpo.repository.PlayerMoveRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class GameControllerTest extends BaseControllerTest {


	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private MoveRepository moveRepository;

	@Autowired
	private PlayerMoveRepository playerMoveRepository;
	
	PlayerDto p1;
	Player player1;
	PlayerDto p2;
	Player player2;
	
	Game gameCriado;
	
	@BeforeAll
	public void setUp() throws JsonProcessingException, Exception {
		this.createUserIfNotExists();
		this.login();
		System.out.println(this.token);

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("acess_1");
        Set<Role> roles = new HashSet<>();
        roles.add(role1);

        p1 = new PlayerDto("John1", "12232", "John");
        p2 = new PlayerDto("Jane1", "12232", "Jane");

        createUserIfNotExists(p1);
        player1 = getPlayer(p1.getUsername());
        System.out.println("P1: " + player1);
        createUserIfNotExists(p2);
        player2 = getPlayer(p2.getUsername());
        System.out.println(player2);

        moveRepository.save(new Move(null, "Spock", null));
        moveRepository.save(new Move(null, "Tesoura", null));
        moveRepository.save(new Move(null, "Papel", null));
        moveRepository.save(new Move(null, "Pedra", null));
        moveRepository.save(new Move(null, "Lagarto", null));
        
	}
	
	@AfterAll
	public void cleanUp() {
		try {
			playerMoveRepository.deleteAll();
			gameRepository.deleteAll();
			moveRepository.deleteAll();
			playerRepository.deleteAll();
		}
		catch (Exception e) {
			
		}
	}
	
	@Test
	@Order(1)
	public void testNewGame() throws Exception {
		
		// Arrange
		List<Long> players = Arrays.asList(player1.getId(), player2.getId());
		GameDto gameDto = new GameDto(players);
		
		LocalDateTime inicio = LocalDateTime.now();
		
		// Act
		mockMvc.perform(post("/api/v1/jokenpo/game/new")
						.header("Authorization", this.getAuthorization())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(gameDto)))
			.andExpect(status().isCreated());
		
		
		MvcResult result = mockMvc.perform(get("/api/v1/jokenpo/game")
				.header("Authorization", this.getAuthorization()))
						.andExpect(status().isOk())
						.andReturn();

		// Assert
		List<Game> games = objectMapper.readValue(
				result.getResponse().getContentAsString(),
				new TypeReference<List<Game>>() {});
		
		System.out.println(games);
		Optional<Game> op = games.stream().filter(g -> g.getCreatedAt().isAfter(inicio) && g.getCreator().getUsername().equals(this.username)).findFirst();
		System.out.println(op.get());
		assertTrue(op.isPresent());
		gameCriado = op.get();
	}

	
	@Test
	@Order(2)
	public void testInsertPlayer1Move() throws Exception {
		
		String token = this.login(p1.getUsername(), p1.getPassword());
		
		GameMoveDto gameMoveDto = new GameMoveDto(gameCriado.getId(), 1L);
		// Act
		MvcResult result = mockMvc.perform(post("/api/v1/jokenpo/game/move")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(gameMoveDto)))
			.andExpect(status().isOk()).andReturn();
		
		ResultDto resultDto = objectMapper.readValue(
				result.getResponse().getContentAsString(),
				new TypeReference<ResultDto>() {});
		System.out.println(resultDto);
	}

	
	@Test
	@Order(3)
	public void testInsertPlayer2Move() throws Exception {
		
		String token = this.login(p2.getUsername(), p2.getPassword());
		
		GameMoveDto gameMoveDto = new GameMoveDto(gameCriado.getId(), 2L);
		// Act
		MvcResult result = mockMvc.perform(post("/api/v1/jokenpo/game/move")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(gameMoveDto)))
			.andExpect(status().isOk()).andReturn();
		
		ResultDto resultDto = objectMapper.readValue(
				result.getResponse().getContentAsString(),
				new TypeReference<ResultDto>() {});
		System.out.println(resultDto);
	}
}
