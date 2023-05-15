package tech.ada.games.jokenpo.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.dto.RankingDto;
import tech.ada.games.jokenpo.model.GameWinner;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.repository.GameWinnerRepository;

@Service
public class RankingService {
	private GameWinnerRepository gameWinnerRepository;

	public RankingService(GameWinnerRepository gameWinnerRepository) {
		this.gameWinnerRepository = gameWinnerRepository;
	}

	public List<RankingDto> getRanking() {
		List<GameWinner> winners = gameWinnerRepository.findAll();
		Map<Player, Long> points = winners.stream()
				.collect(Collectors.groupingBy(x -> x.getPlayer(), Collectors.counting()));
		List<RankingDto> ranking = points.entrySet().stream()
				.map(x -> RankingDto.builder().player(new PlayerDto(x.getKey())).points(x.getValue()).build())
				.collect(Collectors.toList());
		Collections.sort(ranking);
		for (int i = 0; i < ranking.size(); i++) {
			ranking.get(i).setPosition(i + 1);
		}

		return ranking;
	}
}
