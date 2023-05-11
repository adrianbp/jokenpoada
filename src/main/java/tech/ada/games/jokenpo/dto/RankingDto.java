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
	private Long playerId;
	private Long points;
	
	@Override
	public int compareTo(RankingDto o) {
		return o.getPoints().compareTo(this.points);
	}
}
