package tech.ada.games.jokenpo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.ada.games.jokenpo.model.GameWinner;
import tech.ada.games.jokenpo.model.GameWinnerId;

public interface GameWinnerRepository extends JpaRepository<GameWinner, GameWinnerId> {

}
