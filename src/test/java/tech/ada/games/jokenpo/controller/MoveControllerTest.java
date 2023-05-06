package tech.ada.games.jokenpo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import tech.ada.games.jokenpo.dto.MoveDto;
import tech.ada.games.jokenpo.model.Move;
import tech.ada.games.jokenpo.repository.MoveRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class MoveControllerTest extends BaseControllerTest {

	@Autowired
	private MoveRepository moveRepository;
	
	@BeforeEach
	public void setUp() throws JsonProcessingException, Exception {
		this.createUserIfNotExists();
		this.login();
	}
	
	@AfterEach
	public void cleanUp() {
		this.moveRepository.deleteAll();
	}
	
	@Test
	public void testCreateMove() throws Exception {
		// Arrange
		MoveDto moveDto = MoveDto.builder().move("Papel").build();
		
		// Act
		mockMvc.perform(post("/api/v1/jokenpo/move")
						.header("Authorization", this.getAuthorization())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(moveDto)))
			.andExpect(status().isCreated());
		
		// Assert
		assertTrue(moveRepository.findByMove("Papel").isPresent());
	}

	
	@Test
	public void testFindMoves() throws Exception {
		// Arrange
		moveRepository.save(Move.builder().move("Spock").build());
		moveRepository.save(Move.builder().move("Tesoura").build());
		
		// Act
		MvcResult result = mockMvc.perform(get("/api/v1/jokenpo/move")
						.header("Authorization", this.getAuthorization()))
			.andExpect(status().isOk())
			.andReturn();
		
		// Assert
		List<Move> moves = objectMapper.readValue(
				result.getResponse().getContentAsString(),
				new TypeReference<List<Move>>() {});
		assertEquals(2, moves.size());
		assertTrue(moves.stream().anyMatch(move -> move.getMove().equals("Spock")));
		assertTrue(moves.stream().anyMatch(move -> move.getMove().equals("Tesoura")));
	}

	@Test
	public void testFindMove() throws Exception {
		// Arrange
		moveRepository.save(Move.builder().move("Spock").build());
		
		// Act
		MvcResult result = mockMvc.perform(get("/api/v1/jokenpo/move/Spock")
						.header("Authorization", this.getAuthorization()))
			.andExpect(status().isOk())
			.andReturn();
		
		// Assert
		Move move = objectMapper.readValue(
				result.getResponse().getContentAsString(),
				Move.class);
		assertEquals("Spock", move.getMove());
	}
}
