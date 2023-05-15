package tech.ada.games.jokenpo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tech.ada.games.jokenpo.model.Player;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
public class PlayerDto {

    private String username;
    private String password;
    private String name;

    public PlayerDto(Player player) {
        this.username = player.getUsername();
        this.name = player.getName();
    }

}
