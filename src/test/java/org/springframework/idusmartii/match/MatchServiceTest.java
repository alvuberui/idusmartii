package org.springframework.idusmartii.match;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.idusmartii.board.Board;
import org.springframework.idusmartii.board.BoardService;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class MatchServiceTest {

	@Autowired
	private MatchService matchService;

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;
	
	
	/*
	 * alvuberui: función auxiliar que devuelve una lista con n jugadores
	 * y hace un save de cada usuario/jugador/autoridades
	 */
	@Transactional
	public List<Player> auxCreateplayers(Integer numPlayers) throws Exception {
		List<Player> players = new ArrayList<Player>();
		
		for(int acum=0; acum < numPlayers; acum++) {
			Player testPlayer = new Player();
			testPlayer.setFirstName("prueba"+acum);
			testPlayer.setLastName("test"+acum);
			testPlayer.setEmail("prueba"+acum+"@gmail.com");
			testPlayer.setUser(null);
			
			User testUser = new User();
		    testUser.setUsername("testUser"+acum);
		    testUser.setPassword("supersecretpassword"+acum);
		    testUser.setEnabled(true);
			testUser.setUsername("testUser"+acum);
			
			testPlayer.setUser(testUser);
			
			try {
				this.playerService.savePlayer(testPlayer);
			} catch (Exception CantSavePlayer) {
				throw new Exception("Hay un problema al guardar el player");
			}
			
			try {
				authoritiesService.saveAuthorities("testUser"+acum,"player");
			} catch (Exception CantSaveAutorithies) {
				throw new Exception("Hay un problema al guardar las autorithies");
			}
			
			try {
				userService.saveUser(testUser);
			} catch (Exception CantSavePlayer) {
				throw new Exception("Hay un problema al guardar el user");
			}
			players.add(testPlayer);
		}
		return players;
	}

	
	/*
	 * alvuberui: se comprueba que funcionan correctamente todos los métodos de 
	 * la clase matchService.
	 */
	@Test
	@Transactional
	public void boardServiceTestComplete() throws Exception {	
		List<Player> players = auxCreateplayers(5);
		Integer numMatchs = matchService.matchCount();
		// ---- BOARD ---- //
		Board board = new Board();
		board.setAnfitrion(players.get(0));
		board.setNumPlayers(players.size());
		
		try {
            this.boardService.saveBoard(board);
        } catch (Exception ex) {
        	throw new Exception("La mesa con id " + board.getId() + " no ha podido ser guardada");
        }
		
		Match match = new Match(players);
		match.setBoard(board);
		
        try {
            this.matchService.save(match);
        } catch (Exception ex) {
            throw new Exception("La partida con id " + match.getId() + " no ha podido ser guardada");
        }
        
		assertThat(match.getId()).isNotNull();

		Collection<Match> matchs1 = (Collection<Match>) matchService.findAll(); 
		Collection<Match> matchsDesc = (Collection<Match>) matchService.findAllDesc(); 
		assertThat(matchs1.contains(match));
		assertThat(matchsDesc.contains(match));
		assertEquals(numMatchs + 1, matchs1.size());
		assertEquals(matchs1.size(), matchService.matchCount());
		
		Integer matchId = match.getId();
		assertThat(match.equals(matchService.findMatchById(matchId).get()));
		
		assertThat(match.equals(matchService.getMatchBoardId(board)));
	}
	
	/*
	 * alvuberui: Test negativo, insertar una partida con menos de los jugadores permitidos
	 */
	@Test
	@Transactional
	public void exceptionMoreThanMaxPlayers() throws Exception {	
		List<Player> players = auxCreateplayers(3);
		
		Board board = new Board();
		board.setAnfitrion(players.get(0));
		board.setNumPlayers(players.size());
		
		try {
            this.boardService.saveBoard(board);
        } catch (Exception ex) {
        	throw new Exception("La mesa con id " + board.getId() + " no ha podido ser guardada");
        }
		
		Match match = new Match(players);
		match.setBoard(board);
		
		Assertions.assertThrows(javax.validation.ConstraintViolationException.class, () ->{
        	this.matchService.save(match);
    	});	
	}
	
	/*
	 * alvuberui: Test negativo, insertar una partida con un valor nulo en una propidedad
	 * con la restricción @NotNull. Board == null
	 */
	@Test
	@Transactional
	public void exceptionPropertyNull() throws Exception {	
		List<Player> players = auxCreateplayers(6);
		
		Match match = new Match(players);
		match.setBoard(null);
		
		Assertions.assertThrows(javax.validation.ConstraintViolationException.class, () ->{
        	this.matchService.save(match);
    	});	
	}
}
