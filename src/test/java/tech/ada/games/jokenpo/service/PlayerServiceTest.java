package tech.ada.games.jokenpo.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.model.Role;
import tech.ada.games.jokenpo.repository.PlayerMoveRepository;
import tech.ada.games.jokenpo.repository.PlayerRepository;
import tech.ada.games.jokenpo.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PlayerMoveRepository playerMoveRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PlayerService playerService;

    public PlayerServiceTest() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreatePlayer() throws DataConflictException {

        PlayerDto playerDto = new PlayerDto("John", "john", "password");

        when(playerRepository.existsByUsername(anyString())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(new Role()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");


        playerService.createPlayer(playerDto);


        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    public void testCreatePlayerWithExistingUsername() {
        // Arrange
        PlayerDto playerDto = new PlayerDto("John", "john", "password");

        when(playerRepository.existsByUsername(anyString())).thenReturn(true);

        // Act and assert
        assertThrows(DataConflictException.class, () -> playerService.createPlayer(playerDto));
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    public void testFindPlayers() throws DataNotFoundException {

        when(playerRepository.findAll()).thenReturn(List.of(new Player()));


        List<Player> players = playerService.findPlayers();


        assertFalse(players.isEmpty());
    }

    @Test
    public void testFindPlayersEmpty() {

        when(playerRepository.findAll()).thenReturn(List.of());


        assertThrows(DataNotFoundException.class, () -> playerService.findPlayers());
    }

    @Test
    public void testFindByPlayer() throws DataNotFoundException {

        Player player = new Player();
        when(playerRepository.findByUsername(anyString())).thenReturn(Optional.of(player));


        Player foundPlayer = playerService.findByPlayer("john");


        assertNotNull(foundPlayer);
    }

    @Test
    public void testFindByNonExistingPlayer() {

        when(playerRepository.findByUsername(anyString())).thenReturn(Optional.empty());


        assertThrows(DataNotFoundException.class, () -> playerService.findByPlayer("john"));
    }

    @Test
    public void testDeletePlayer() throws DataNotFoundException, DataConflictException {

        Player player = new Player();
        player.setId(1L);
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));
        when(playerMoveRepository.countByUnfinishedGameAndPlayer(anyLong())).thenReturn(0L);

        playerService.deletePlayer(player.getId());


        verify(playerRepository, times(1)).delete(any(Player.class));

    }

    @Test
    public void testDeleteNonExistingPlayer()  {

        Player player = new Player();
        player.setId(1L);
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());


        assertThrows(DataNotFoundException.class, () -> playerService.deletePlayer(player.getId()));

    }

    @Test
    public void testDeleteUnfinishedGamePlayer() {

        Player player = new Player();
        player.setId(1L);
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));
        when(playerMoveRepository.countByUnfinishedGameAndPlayer(anyLong())).thenReturn(1L);


        assertThrows(DataConflictException.class, () -> playerService.deletePlayer(player.getId()));

    }

}
