package tech.ada.games.jokenpo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

import tech.ada.games.jokenpo.dto.MoveDto;
import tech.ada.games.jokenpo.exception.BadRequestException;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.model.Move;
import tech.ada.games.jokenpo.repository.MoveRepository;

public class MoveServiceTest {
	private MoveRepository moveRepository = mock(MoveRepository.class);
	private MoveService moveService = new MoveService(moveRepository);
	
	private ArgumentCaptor<Move> moveCaptor = ArgumentCaptor.forClass(Move.class);
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Spock",
			"Tesoura",
			"Papel",
			"Pedra",
			"Lagarto",
			"Jogada Spock",
			"Jogada Tesoura",
			"Jogada Papel",
			"Jogada Pedra",
			"Jogada Lagarto",
			"SPOCK",
			"tesoura",
			"PaPeL",
			"pEdRa",
			"LAGarto",
			"JOGADA SPOCK",
			"jogada tesoura",
			"JoGaDa PaPeL",
			"jOgAdA pEdRa",
			"JOGada LAGArto"
	})
	public void testCreateMoveValid(String moveStr) throws DataConflictException, BadRequestException {
		// Arrange
		MoveDto moveDto = new MoveDto(moveStr);
		
		when(moveRepository.existsByMove(moveStr)).thenReturn(false);
		
		// Act
		moveService.createMove(moveDto);
		
		// Assert
		verify(moveRepository).save(moveCaptor.capture());
		assertEquals(moveStr, moveCaptor.getValue().getMove());
	}
}
