package tech.ada.games.jokenpo.acceptance.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.dto.RankingDto;
import tech.ada.games.jokenpo.model.GameWinner;
import tech.ada.games.jokenpo.model.GameWinnerId;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.repository.GameWinnerRepository;
import tech.ada.games.jokenpo.service.RankingService;

public class RankingSteps {
	private GameWinnerRepository gameWinnerRepository = mock(GameWinnerRepository.class);
	private RankingService rankingService;

	private Player player1 = Player.builder().id(1l).username("player1").build();
	private Player player2 = Player.builder().id(2l).username("player2").build();
	private Player player3 = Player.builder().id(3l).username("player3").build();
	private Player player4 = Player.builder().id(4l).username("player4").build();
	private Player player5 = Player.builder().id(5l).username("player5").build();
	private List<RankingDto> genericRanking;
	
	public RankingSteps() {
		rankingService = new RankingService(gameWinnerRepository);
	}
	
    @Dado("um usuario qualquer")
    public void dado_usuario_qualquer() {
		when(gameWinnerRepository.findAll()).thenReturn(List.of(
				new GameWinner(new GameWinnerId(1L, player1.getId()), player1),
				new GameWinner(new GameWinnerId(2L, player1.getId()), player1),
				new GameWinner(new GameWinnerId(3L, player3.getId()), player3),
				new GameWinner(new GameWinnerId(4L, player2.getId()), player2),
				new GameWinner(new GameWinnerId(5L, player4.getId()), player4),
				new GameWinner(new GameWinnerId(5L, player1.getId()), player1),
				new GameWinner(new GameWinnerId(6L, player2.getId()), player2),
				new GameWinner(new GameWinnerId(7L, player5.getId()), player5),
				new GameWinner(new GameWinnerId(7L, player2.getId()), player2),
				new GameWinner(new GameWinnerId(8L, player1.getId()), player1)));
    }

    @Quando("busca o ranking dos jogadores")
    public void busca_o_ranking_dos_jogadores() {
    	genericRanking = rankingService.getRanking();
    }
    
    @Entao("recebe uma lista")
    public void recebe_uma_lista() {
		assertEquals(5, genericRanking.size());
		assertEquals(new RankingDto(1, new PlayerDto(player1), 4L), genericRanking.get(0));
		assertEquals(new RankingDto(2, new PlayerDto(player2), 3L), genericRanking.get(1));
		assertEquals(new RankingDto(3, new PlayerDto(player3), 1L), genericRanking.get(2));
		assertEquals(new RankingDto(4, new PlayerDto(player4), 1L), genericRanking.get(3));
		assertEquals(new RankingDto(5, new PlayerDto(player5), 1L), genericRanking.get(4));
    }

}
