package tech.ada.games.jokenpo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tech.ada.games.jokenpo.model.Player;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlayerDto {

    private String username;
    private String password;
    private String name;

    public PlayerDto(Player player) {
        this.username = player.getUsername();
        this.name = player.getName();
    }

}
