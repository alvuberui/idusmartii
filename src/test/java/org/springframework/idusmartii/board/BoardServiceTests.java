package org.springframework.idusmartii.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.user.Authorities;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class BoardServiceTests {

	@Autowired
	protected BoardService boardService;
	
	@Autowired
	protected AuthoritiesService authoritiesService;
	
	@Autowired
	protected UserService userService;
	

	@Autowired
	protected PlayerService playerService;
	
	/*
	 * alvuberui: test negativo, debe de saltar una excepcion al dejar la propiedad
	 * numPlayers con valor null
	 */
	
	@Test
	@Transactional
	public void ExceptionNumPlayerNull() throws Exception {		
		//-------- PLAYER --------//
		Player testPlayer = new Player();
		testPlayer.setFirstName("prueba");
		testPlayer.setLastName("test");
		testPlayer.setEmail("prueba@gmail.com");
		testPlayer.setUser(null);
		
		//-------- USER --------//
		User testUser = new User();
	    testUser.setUsername("testUser");
	    testUser.setPassword("supersecretpassword");
	    testUser.setEnabled(true);
		testUser.setUsername("testUser");
		
		testPlayer.setUser(testUser);

		//-------- SAVE --------//
		
		try {
			this.playerService.savePlayer(testPlayer);
		} catch (Exception CantSavePlayer) {
			throw new Exception("Hay un problema al guardar el player");
		}
		
		try {
			userService.saveUser(testUser);
		} catch (Exception CantSavePlayer) {
			throw new Exception("Hay un problema al guardar el user");
		}
		
		
		//-------- BOARD --------//
		Board board = new Board();
		board.setAnfitrion(testPlayer);
		board.setNumPlayers(null);		
		
		//-------- authorities --------//		
		
		try {
			authoritiesService.saveAuthorities("testUser","player");
		} catch (Exception CantSaveAutorithies) {
			throw new Exception("Hay un problema al guardar las autorithies");
		}
		
		//-------- SAVE BOARD --------// Prueba guardar mesa con numPlayers = null
        Assertions.assertThrows(javax.validation.ConstraintViolationException.class, () ->{
        	this.boardService.saveBoard(board);
    		});
	}
	
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
	 * alvuberui: Test negativo, insertar una mesa con más jugadores de lo permitido (8)
	 */
	@Test
	@Transactional
	public void exceptionMoreThanMaxPlayers() throws Exception {	
		List<Player> players = auxCreateplayers(9);
		
		Board board = new Board();
		board.setAnfitrion(players.get(0));
		board.setNumPlayers(players.size());
		
		Assertions.assertThrows(javax.validation.ConstraintViolationException.class, () ->{
        	this.boardService.saveBoard(board);
    	});	
	}
	
	/*
	 * alvuberui: se comprueban correctamente todos los metodos del servicio board
	 */
	@Test
	@Transactional
	public void boardServiceTestComplete() throws Exception {		
		List<Player> players = auxCreateplayers(1);
		
		Board board = new Board(1);
		board.setAnfitrion(players.get(0));
		Integer count1 = boardService.boardCount();
		
        try {
            this.boardService.saveBoard(board);
        } catch (Exception ex) {
        	throw new Exception("La mesa con id " + board.getId() + " no ha podido ser guardada");
        }
        Integer id = board.getId();
		assertThat(board.getId()).isNotNull(); //Prueba generar id auto y buscar por id
		assertThat(board.equals(boardService.findById(id).get()));    
		
		Collection<Board> boards1 = (Collection<Board>) boardService.findAll(); //buscar todas las mesas
		assertThat(boards1.contains(board));
		
		Integer count2 = boardService.boardCount();
		assertEquals(count1+1, count2);
		
		try { //Prueba eliminar mesa
            this.boardService.deleteBoard(board.getId());
        } catch (Exception ex) {
        	throw new Exception("La mesa con id " + board.getId() + " no ha podido ser eliminada");
        }
		
		Collection<Board> boards2 = (Collection<Board>) boardService.findAll();
		assertThat(!boards2.contains(board));
		assertEquals(boards2.size() +1, boards1.size());
	}

}
