package tech.ada.games.jokenpo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import tech.ada.games.jokenpo.dto.GameMoveDto;
import tech.ada.games.jokenpo.dto.ResultDto;
import tech.ada.games.jokenpo.model.Game;
import tech.ada.games.jokenpo.model.Move;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.model.PlayerMove;
import tech.ada.games.jokenpo.repository.GameRepository;
import tech.ada.games.jokenpo.repository.MoveRepository;
import tech.ada.games.jokenpo.repository.PlayerMoveRepository;
import tech.ada.games.jokenpo.repository.PlayerRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GameServiceResultsTest {

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
	GameMoveDto gameMove;
	Game game;
	Optional<Game> opGame;
	Optional<Move> opMove;
	PlayerMove playerMove0;
	Optional<PlayerMove> opPlayerMove;
	
	Move move1;
	PlayerMove playerMove1;
	List<PlayerMove> playerMoves;
	
	String username = null;
	
	@BeforeEach
	void createUser() {
		player1 = new Player(1L, "user1", "pass", "user1", null);
		opPlayer1 = Optional.of(player1);
		player2 = new Player(2L, "user2", "pass", "user2", null);
		opPlayer2 = Optional.of(player2);
		gameMove = new GameMoveDto();
		gameMove.setGameId(1L);
		
		game = new Game();
		game.setId(1L);
		game.setFinished(false);
		opGame = Optional.of(game);
		

	    gameMove.setMoveId(1L);
	    Move move0 = new Move();
	    move0.setId(1L);
	    move0.setMove("Spock");
	    opMove = Optional.of(move0);
		
	    playerMove0 = new PlayerMove();
	    playerMove0.setId(1L);
	    playerMove0.setGame(game);
	    playerMove0.setPlayer(player1);
	    opPlayerMove = Optional.of(playerMove0);
	    
	    move1 = new Move();
	    move1.setId(1L);
	    move1.setMove("Spock");
	    playerMove1 = new PlayerMove();
	    playerMove1.setId(1L);
	    playerMove1.setGame(game);
	    playerMove1.setPlayer(player1);
	    playerMove1.setMove(move1);
	    
	    playerMoves = Arrays.asList(playerMove1);
	    
	    when(playerRepository.findByUsername(username)).thenReturn(opPlayer1);
		when(gameRepository.findById(gameMove.getGameId())).thenReturn(opGame);
	    when(moveRepository.findById(gameMove.getMoveId())).thenReturn(opMove);
	    when(playerMoveRepository.findByUnfinishedGameIdAndPlayer(player1.getId(), gameMove.getGameId())).thenReturn(opPlayerMove);
	    when(playerMoveRepository.countMovesPlayedByUnfinishedGame(1L)).thenReturn(2L);
        when(playerMoveRepository.countByUnfinishedGameId(1L)).thenReturn(2L);
	    
        when(playerMoveRepository.findByUnfinishedGameId(1L, "Spock")).thenReturn(playerMoves);
        when(playerMoveRepository.findByUnfinishedGameId(1L, "Tesoura")).thenReturn(playerMoves);
        when(playerMoveRepository.findByUnfinishedGameId(1L, "Papel")).thenReturn(playerMoves);
        when(playerMoveRepository.findByUnfinishedGameId(1L, "Pedra")).thenReturn(playerMoves);
        when(playerMoveRepository.findByUnfinishedGameId(1L, "Lagarto")).thenReturn(playerMoves);
	}
	
	
	@Test
	void insertPlayerMoveResultSpockTesouraTest() {

	    move1.setMove("Spock");
        
        when(playerMoveRepository.existsSpockByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsTesouraByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsPapelByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsPedraByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsLagartoByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
	    
        
	    ResultDto resultado = null;
	    try {
	    	resultado = gameService.insertPlayerMove(gameMove);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	    System.out.println(resultado);
	    assertEquals("Spock", resultado.getOriginalResult());
	    
	}
	
	@Test
	void insertPlayerMoveResultSpockPapelTest() {

	    move1.setMove("Spock");
        
        when(playerMoveRepository.existsSpockByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsTesouraByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsPapelByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsPedraByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsLagartoByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
	    
        
	    ResultDto resultado = null;
	    try {
	    	resultado = gameService.insertPlayerMove(gameMove);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	    System.out.println(resultado);
	    assertEquals("Papel", resultado.getOriginalResult());
	    
	}
	
	@Test
	void insertPlayerMoveResultSpockPedraTest() {

	    move1.setMove("Spock");
        
        when(playerMoveRepository.existsSpockByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsTesouraByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsPapelByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsPedraByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsLagartoByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
	    
        
	    ResultDto resultado = null;
	    try {
	    	resultado = gameService.insertPlayerMove(gameMove);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	    System.out.println(resultado);
	    assertEquals("Spock", resultado.getOriginalResult());
	    
	}
	
	@Test
	void insertPlayerMoveResultSpockLagartoTest() {

	    move1.setMove("Spock");
        
        when(playerMoveRepository.existsSpockByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsTesouraByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsPapelByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsPedraByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsLagartoByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
	    
        
	    ResultDto resultado = null;
	    try {
	    	resultado = gameService.insertPlayerMove(gameMove);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	    System.out.println(resultado);
	    assertEquals("Lagarto", resultado.getOriginalResult());
	    
	}
	
	@Test
	void insertPlayerMoveResultTesouraPapelTest() {

	    move1.setMove("Tesoura");

        when(playerMoveRepository.existsSpockByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsTesouraByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsPapelByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsPedraByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsLagartoByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
	    
        
        ResultDto resultado = null;
	    try {
	    	resultado = gameService.insertPlayerMove(gameMove);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	    System.out.println(resultado);
	    assertEquals("Tesoura", resultado.getOriginalResult());
	    
	}
	
	@Test
	void insertPlayerMoveResultTesouraPedraTest() {

	    move1.setMove("Tesoura");

        when(playerMoveRepository.existsSpockByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsTesouraByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsPapelByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsPedraByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsLagartoByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
	    
        
        ResultDto resultado = null;
	    try {
	    	resultado = gameService.insertPlayerMove(gameMove);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	    System.out.println(resultado);
	    assertEquals("Pedra", resultado.getOriginalResult());
	    
	}
	
	@Test
	void insertPlayerMoveResultTesouraLagartoTest() {

	    move1.setMove("Tesoura");

        when(playerMoveRepository.existsSpockByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsTesouraByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsPapelByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsPedraByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsLagartoByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
	    
        
        ResultDto resultado = null;
	    try {
	    	resultado = gameService.insertPlayerMove(gameMove);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	    System.out.println(resultado);
	    assertEquals("Tesoura", resultado.getOriginalResult());
	    
	}
	
	@Test
	void insertPlayerMoveResultPapelPedraTest() {

	    move1.setMove("Papel");

        when(playerMoveRepository.existsSpockByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsTesouraByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsPapelByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsPedraByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsLagartoByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
	    
        
        ResultDto resultado = null;
	    try {
	    	resultado = gameService.insertPlayerMove(gameMove);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	    System.out.println(resultado);
	    assertEquals("Papel", resultado.getOriginalResult());
	    
	}
	
	@Test
	void insertPlayerMoveResultPapelLagartoTest() {

	    move1.setMove("Papel");

        when(playerMoveRepository.existsSpockByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsTesouraByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsPapelByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsPedraByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsLagartoByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
	    
        
        ResultDto resultado = null;
	    try {
	    	resultado = gameService.insertPlayerMove(gameMove);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	    System.out.println(resultado);
	    assertEquals("Lagarto", resultado.getOriginalResult());
	    
	}
	
	@Test
	void insertPlayerMoveResultPedraLagartoTest() {

	    move1.setMove("Pedra");

        when(playerMoveRepository.existsSpockByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsTesouraByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsPapelByUnfinishedGameId(1L)).thenReturn(Boolean.FALSE);
        when(playerMoveRepository.existsPedraByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
        when(playerMoveRepository.existsLagartoByUnfinishedGameId(1L)).thenReturn(Boolean.TRUE);
	    
        
        ResultDto resultado = null;
	    try {
	    	resultado = gameService.insertPlayerMove(gameMove);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	    System.out.println(resultado);
	    assertEquals("Pedra", resultado.getOriginalResult());
	    
	}
}
