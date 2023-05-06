package tech.ada.games.jokenpo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

import tech.ada.games.jokenpo.dto.MoveDto;
import tech.ada.games.jokenpo.exception.BadRequestException;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.Move;
import tech.ada.games.jokenpo.repository.MoveRepository;

public class MoveServiceTest {
	private MoveRepository moveRepository = mock(MoveRepository.class);
	private MoveService moveService = new MoveService(moveRepository);
	
	private ArgumentCaptor<Move> moveCaptor = ArgumentCaptor.forClass(Move.class);
	
	@Test
	public void testCreateMoveAlreadyExists() throws DataConflictException, BadRequestException {
		// Arrange
		String moveStr = "Jogada";
		MoveDto moveDto = new MoveDto(moveStr);
		
		when(moveRepository.existsByMove(moveStr)).thenReturn(true);
		
		// Act
		DataConflictException ex = assertThrows(DataConflictException.class, () -> moveService.createMove(moveDto));
		
		// Assert
		assertEquals("A jogada já está cadastrada!", ex.getMessage());
	}
	
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
	
	@Test
	public void testCreateMoveInvalid() throws DataConflictException, BadRequestException {
		// Arrange
		String moveStr = "Jogada";
		MoveDto moveDto = new MoveDto(moveStr);
		
		when(moveRepository.existsByMove(moveStr)).thenReturn(false);
		
		// Act
		BadRequestException ex = assertThrows(BadRequestException.class, () -> moveService.createMove(moveDto));
		
		// Assert
		assertEquals("Você pode cadastrar apenas os movimentos Spock, Tesoura, Papel, Pedra e Lagarto", ex.getMessage());
	}
	
	@Test
	public void testFindMoves() throws DataNotFoundException {
		// Arrange
		List<Move> moves = List.of(new Move());
		when(moveRepository.findAll()).thenReturn(moves);
		
		// Act
		List<Move> actualMoves = moveService.findMoves();
		
		// Assert
		assertEquals(moves, actualMoves);
	}

	@Test
	public void testFindMovesEmpty() throws DataNotFoundException {
		// Arrange
		when(moveRepository.findAll()).thenReturn(List.of());
		
		// Act
		DataNotFoundException ex = assertThrows(DataNotFoundException.class, () -> moveService.findMoves());
		
		// Assert
		assertEquals("Não há jogadas cadastradas!", ex.getMessage());
	}

	@Test
	public void testFindByMove() throws DataNotFoundException {
		// Arrange
		String moveStr = "Jogada";
		Move move = new Move();
		move.setMove(moveStr);
		
		when(moveRepository.findByMove(moveStr)).thenReturn(Optional.of(move));
		
		// Act
		Move actualMove = moveService.findByMove(moveStr);
		
		// Assert
		assertEquals(move, actualMove);
	}

	@Test
	public void testFindByMoveEmpty() throws DataNotFoundException {
		// Arrange
		String moveStr = "Jogada";
		when(moveRepository.findByMove(moveStr)).thenReturn(Optional.empty());
		
		// Act
		DataNotFoundException ex = assertThrows(DataNotFoundException.class, () -> moveService.findByMove(moveStr));
		
		// Assert
		assertEquals("A jogada não está cadastrada!", ex.getMessage());
	}
}
