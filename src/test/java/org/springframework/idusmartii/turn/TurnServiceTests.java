package org.springframework.idusmartii.turn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.idusmartii.board.Board;
import org.springframework.idusmartii.board.BoardService;
import org.springframework.idusmartii.consul.Consul;
import org.springframework.idusmartii.consul.ConsulService;
import org.springframework.idusmartii.edil.Edil;
import org.springframework.idusmartii.edil.EdilService;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchService;
import org.springframework.idusmartii.match.MatchTurnStatus;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.turn.Turn;
import org.springframework.idusmartii.turn.TurnService;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TurnServiceTests {
	
	
	@Autowired
	private EdilService edilService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private AuthoritiesService authoritiesService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ConsulService consulService;
	
	@Autowired
	private TurnService turnService;
	
	@Autowired
	private MatchService matchService;
	
	@Autowired
	private BoardService boardService;
	

	/*
	 * alvuberui: método auxiliar para crear n players automáticamente
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
	 * alvuberui: Se prueban al completo todos los métodos de la clase TurnService
	 */
	@Test
	@Transactional
	public void EdilServiceTestComplete() throws Exception {	
		Integer numTurns1 = turnService.turnCount();
		List<Player> players = auxCreateplayers(5);
		
		Consul consul = new Consul(players.get(0));
		try {
            this.consulService.save(consul);
        } catch (Exception ex) {
            throw new Exception("El consul no ha podido ser guardado");
        }
		
		Board board = new Board(players.size());
		board.setAnfitrion(players.get(0));
		try {
            this.boardService.saveBoard(board);
        } catch (Exception ex) {
            throw new Exception("La mesa no ha podido ser guardado");
        }
		
		Match match = new Match(players);
		match.setBoard(board);
		try {
            this.matchService.save(match);
        } catch (Exception ex) {
            throw new Exception("La partida no ha podido ser guardado");
        }
		
		Turn turn = new Turn(match, 1, consul);
		turn.setMatchTurnStatus(MatchTurnStatus.CHOOSE_ROL);
		turn.setNextStateTurn(LocalTime.now().plusSeconds(10));
		try {
            this.turnService.save(turn);
        } catch (Exception ex) {
            throw new Exception("El turno no ha podido ser guardado");
        }
	
		assertThat(turn.getId()).isNotNull();
		
		Collection<Turn> turns1 = (Collection<Turn>) turnService.findAll(); 
		assertThat(turns1.contains(turn));
		Integer numTurns2 = turnService.turnCount();
		assertEquals(turns1.size(), numTurns2, "El número de turns2 no coincide con el numero de turns");
		assertEquals(numTurns1 +1, numTurns2, "El número de turns2 no coincide con turns1 +1");
		
		List<Turn> turns = (List<Turn>) turnService.findTurnByMatch(match);
		assertEquals(turns.get(0), turn, "El turno no coincide con el busquedo");
		
		Integer turnId = turn.getId();
		assertEquals(turn, turnService.findTurnById(turnId).get());
		
		try { 
            this.turnService.delete(turn);
        } catch (Exception ex) {
        	throw new Exception("El turno no ha podido ser eliminado");
        }
		Integer numTurns3 = turnService.turnCount();
		Collection<Turn> turns2 = (Collection<Turn>) turnService.findAll();
		assertEquals(numTurns1 , numTurns3, "El número de turns3 no coincide con turns1");
		assertEquals(numTurns2-1 , numTurns3, "El número de turns3 no coincide con turns2 -1");
		assertThat(!turns2.contains(turn));
		assertEquals(turns2.size() +1, turns1.size());
	}
	
	/*
	 * alvuberui: test negativo, debe saltar una excepcion debido a que se deja
	 * una propiedad con valor null, en este caso match.
	 */
	@Test
	@Transactional
	public void shouldExceptionNullValue() throws Exception {	
		List<Player> players = auxCreateplayers(5);
		Consul consul = new Consul(players.get(0));
		try {
            this.consulService.save(consul);
        } catch (Exception ex) {
            throw new Exception("El consul no ha podido ser guardado");
        }
		
		Turn turn = new Turn(null, 1, consul);
		turn.setMatchTurnStatus(MatchTurnStatus.CHOOSE_ROL);
		turn.setNextStateTurn(LocalTime.now().plusSeconds(10));
		Assertions.assertThrows(javax.validation.ConstraintViolationException.class, () ->{
        	this.turnService.save(turn);
    	});
	}

}
