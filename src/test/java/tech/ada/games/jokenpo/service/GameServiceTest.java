package tech.ada.games.jokenpo.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import tech.ada.games.jokenpo.dto.GameMoveDto;
import tech.ada.games.jokenpo.dto.ResultDto;
import tech.ada.games.jokenpo.exception.BadRequestException;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.Game;
import tech.ada.games.jokenpo.model.Move;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.model.PlayerMove;
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
	void newGame() {
		
		GameDto game = new GameDto();
		game.setPlayers(new ArrayList<>());
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
	
	@Test
	void insertPlayerMoveTest() {
		GameMoveDto gameMove = new GameMoveDto();
		gameMove.setGameId(1L);
		
		Game game = new Game();
		game.setId(1L);
		game.setFinished(true);
		Optional<Game> opGame = Optional.of(game);
		
		String username = null;
		
		//Testando login não encontrado
	    Exception exception = assertThrows(DataNotFoundException.class, () -> gameService.insertPlayerMove(gameMove));
	    String expectedMessage = "O jogador não está cadastrado!";
	    String actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	    
	    //testando login encontrado, mas jogo não cadastrado
		when(playerRepository.findByUsername(username)).thenReturn(opPlayer1);
		exception = assertThrows(DataNotFoundException.class, () -> gameService.insertPlayerMove(gameMove));
		expectedMessage = "Jogo não cadastrado!";
	    actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));


		//Testando com jogo finalizado
	    when(gameRepository.findById(gameMove.getGameId())).thenReturn(opGame);
		exception = assertThrows(BadRequestException.class, () -> gameService.insertPlayerMove(gameMove));
		expectedMessage = "O jogo já foi finalizado!";
	    actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	    
	    //Testando com jogo aberto
	    game.setFinished(false);
	    
	    //testando com jogada não cadastrada
		exception = assertThrows(DataNotFoundException.class, () -> gameService.insertPlayerMove(gameMove));
		expectedMessage = "Jogada não cadastrada";
	    actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	    
	    //Testando com jogada cadastrada
	    gameMove.setMoveId(1L);
	    Move move = new Move();
	    move.setId(1L);
	    move.setMove("SPOCK");
	    Optional<Move> opMove = Optional.of(move);
	    when(moveRepository.findById(gameMove.getMoveId())).thenReturn(opMove);
		exception = assertThrows(DataNotFoundException.class, () -> gameService.insertPlayerMove(gameMove));
		expectedMessage = "Jogador não está cadastrado no jogo!";
	    actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	    
	    //Testando jogador já fez jogada
	    PlayerMove playerMove = new PlayerMove();
	    playerMove.setId(1L);
	    playerMove.setGame(game);
	    playerMove.setMove(move);
	    playerMove.setPlayer(player1);
	    Optional<PlayerMove> opPlayerMove = Optional.of(playerMove);
	    when(playerMoveRepository.findByUnfinishedGameIdAndPlayer(player1.getId(), gameMove.getGameId())).thenReturn(opPlayerMove);
	    
	    exception = assertThrows(DataConflictException.class, () -> gameService.insertPlayerMove(gameMove));
		expectedMessage = "Jogador já realizou a sua jogada!";
	    actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	    
	    //Testando com jogador não fez a jogada
	    playerMove.setMove(null);
	    
	    ResultDto resultado = null;
	    try {
	    	resultado = gameService.insertPlayerMove(gameMove);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    System.out.println(resultado);
	}
}
