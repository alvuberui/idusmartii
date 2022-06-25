package org.springframework.idusmartii.faction;

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
import org.springframework.idusmartii.edil.Edil;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchService;
import org.springframework.idusmartii.match.MatchTurnStatus;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.turn.Turn;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class FactionServiceTest {
	
	@Autowired
	private FactionService factionService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private MatchService matchService;
	
	@Autowired
	private AuthoritiesService authoritiesService;
	
	@Autowired
	private UserService userService;
	
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
	 * alvuberui: Se prueban al completo todos los métodos de la clase FactionService
	 */
	@Test
	@Transactional
	public void FactionServiceTestComplete() throws Exception {	
		Integer numFactions1 = factionService.voteCount();
		List<Player> players = auxCreateplayers(5);
		
		Board board = new Board(players.size());
		try {
            this.boardService.saveBoard(board);
        } catch (Exception ex) {
            throw new Exception("La mesa no ha podido ser guardada");
        }
		
		Match match = new Match(players);
		match.setBoard(board);
		try {
            this.matchService.save(match);
        } catch (Exception ex) {
            throw new Exception("La partida no ha podido ser guardada");
        }
		
		FactionCard faction = new FactionCard();
		faction.setCardType(Faction.LOYAL);
		faction.setMatch(match);
		faction.setPlayer(players.get(0));
		faction.setVoted(false);
		
        try {
            this.factionService.save(faction);
        } catch (Exception ex) {
            throw new Exception("La carta de facción no ha podido ser guardada");
        }
        
		assertThat(faction.getId()).isNotNull();
		Collection<FactionCard> factions1 = (Collection<FactionCard>) factionService.findAll(); 
		assertThat(factions1.contains(faction));
		Integer numFactions2 = factionService.voteCount();
		assertEquals(factions1.size(), numFactions2, "El número de factions2 no coincide con el numero de factions");
		assertEquals(numFactions1 + 1, numFactions2, "El número de factions2 no coincide con factions1 +1");
		
		List<FactionCard> fac = (List<FactionCard>) factionService.findByMatchAndPlayer(match, players.get(0));
		assertEquals(fac.get(0).getId(), faction.getId(), "La id de la carta no coincide con la buscada");
		Integer factionId = faction.getId();
		assertEquals(faction, factionService.findFactionCardById(factionId).get());
		try { 
            this.factionService.delete(faction);
        } catch (Exception ex) {
        	throw new Exception("La faccion no ha podido ser eliminada");
        }
		
		
		Integer numFactions3 = factionService.voteCount();
		Collection<FactionCard> factions2 = (Collection<FactionCard>) factionService.findAll();
		assertEquals(numFactions1 , numFactions3, "El número de factions3 no coincide con factions1");
		assertEquals(numFactions2 -1 , numFactions3, "El número de factions3 no coincide con factions2 -1");
		assertThat(!factions2.contains(faction));
		assertEquals(factions2.size() +1, factions1.size());
	}
	


}
