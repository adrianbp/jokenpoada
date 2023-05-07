package tech.ada.games.jokenpo.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tech.ada.games.jokenpo.dto.GameDto;
import tech.ada.games.jokenpo.exception.BadRequestException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.repository.GameRepository;
import tech.ada.games.jokenpo.repository.MoveRepository;
import tech.ada.games.jokenpo.repository.PlayerMoveRepository;
import tech.ada.games.jokenpo.repository.PlayerRepository;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

	@InjectMocks
	GameService gameService;

	@Mock
    GameRepository gameRepository;

	@Mock
    PlayerMoveRepository playerMoveRepository;

	@Mock
    MoveRepository moveRepository;

	@Mock
    PlayerRepository playerRepository;

	Player player1;
	Optional<Player> opPlayer1;
	Player player2;
	Optional<Player> opPlayer2;
	
	@BeforeEach
	void createUser() {
		player1 = new Player(1L, "user1", "pass", "user1", null);
		opPlayer1 = Optional.of(player1);
		player2 = new Player(2L, "user2", "pass", "user2", null);
		opPlayer2 = Optional.of(player2);
	}
	
	@Test
	void newGameLessPlayers() {
		
		
		GameDto game = new GameDto();
		game.setPlayers(new ArrayList<>());
		//when(SecurityUtils.getCurrentUserLogin()).thenReturn(player.getUsername());
		String username = null;
		
		//Testando login não encontrado
	    Exception exception = assertThrows(DataNotFoundException.class, () -> gameService.newGame(game));
	    String expectedMessage = "O jogador não está cadastrado!";
	    String actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));

	    //testando login encontrado, mas sem jogadores no jogo
		when(playerRepository.findByUsername(username)).thenReturn(opPlayer1);
		
		exception = assertThrows(BadRequestException.class, () -> gameService.newGame(game));
		expectedMessage = "O jogo possui menos que dois jogadores!";
	    actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));

	    //testando com somente um jogador no jogo
	    game.getPlayers().add(1L);
		
		exception = assertThrows(BadRequestException.class, () -> gameService.newGame(game));
		expectedMessage = "O jogo possui menos que dois jogadores!";
	    actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));

	    //testando com dois jogadores no jogo, sem cadastro
	    game.getPlayers().add(2L);
	    exception = assertThrows(DataNotFoundException.class, () -> gameService.newGame(game));
	    expectedMessage = "O jogador não está cadastrado!";
	    actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));

	    //testando com dois jogadores no jogo
	    game.getPlayers().add(2L);

		when(playerRepository.findById(player1.getId())).thenReturn(opPlayer1);
		when(playerRepository.findById(player2.getId())).thenReturn(opPlayer2);
		
		assertDoesNotThrow(() -> gameService.newGame(game));
		
	    
	}
}
