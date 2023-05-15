package tech.ada.games.jokenpo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class RankingDto implements Comparable<RankingDto> {

	private Integer position;
	private PlayerDto player;
	private Long points;
	
	@Override
	public int compareTo(RankingDto o) {
		int comparePoints = o.getPoints().compareTo(this.points);
		if (comparePoints != 0) {
			return comparePoints;
		}
		return this.getPlayer().getUsername().compareTo(o.getPlayer().getUsername());
	}
}
