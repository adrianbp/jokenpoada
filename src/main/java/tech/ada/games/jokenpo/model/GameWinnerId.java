package tech.ada.games.jokenpo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class GameWinnerId {

	@Column(name = "game_id")
	private Long gameId;
	
	@Column(name = "player_id")
	private Long playerId;
}
