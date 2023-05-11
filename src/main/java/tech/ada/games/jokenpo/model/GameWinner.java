package tech.ada.games.jokenpo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game_winner")
public class GameWinner {

    @EmbeddedId
    private GameWinnerId id;
}
