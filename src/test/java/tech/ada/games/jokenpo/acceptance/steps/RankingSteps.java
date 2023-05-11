package tech.ada.games.jokenpo.acceptance.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import tech.ada.games.jokenpo.dto.RankingDto;
import tech.ada.games.jokenpo.model.GameWinner;
import tech.ada.games.jokenpo.model.GameWinnerId;
import tech.ada.games.jokenpo.repository.GameWinnerRepository;
import tech.ada.games.jokenpo.service.RankingService;

public class RankingSteps {
	private GameWinnerRepository gameWinnerRepository = mock(GameWinnerRepository.class);
	private RankingService rankingService;
	List<RankingDto> genericRanking;
	
	public RankingSteps() {
		rankingService = new RankingService(gameWinnerRepository);
	}
	
    @Dado("um usuario qualquer")
    public void dado_usuario_qualquer() {
		when(gameWinnerRepository.findAll()).thenReturn(List.of(
				new GameWinner(new GameWinnerId(1L, 1L)),
				new GameWinner(new GameWinnerId(2L, 1L)),
				new GameWinner(new GameWinnerId(3L, 3L)),
				new GameWinner(new GameWinnerId(4L, 2L)),
				new GameWinner(new GameWinnerId(5L, 4L)),
				new GameWinner(new GameWinnerId(5L, 1L)),
				new GameWinner(new GameWinnerId(6L, 2L)),
				new GameWinner(new GameWinnerId(7L, 5L)),
				new GameWinner(new GameWinnerId(7L, 2L)),
				new GameWinner(new GameWinnerId(8L, 1L))));
    }

    @Quando("busca o ranking dos jogadores")
    public void busca_o_ranking_dos_jogadores() {
    	genericRanking = rankingService.getRanking();
    }
    
    @Entao("recebe uma lista")
    public void recebe_uma_lista() {
		assertEquals(5, genericRanking.size());
		assertEquals(new RankingDto(1, 1L, 4L), genericRanking.get(0));
		assertEquals(new RankingDto(2, 2L, 3L), genericRanking.get(1));
		assertEquals(new RankingDto(3, 3L, 1L), genericRanking.get(2));
		assertEquals(new RankingDto(4, 4L, 1L), genericRanking.get(3));
		assertEquals(new RankingDto(5, 5L, 1L), genericRanking.get(4));
    }

}
