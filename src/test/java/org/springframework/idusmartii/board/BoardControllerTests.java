package org.springframework.idusmartii.board;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.idusmartii.configuration.SecurityConfiguration;
import org.springframework.idusmartii.friends.Friend;
import org.springframework.idusmartii.friends.FriendService;
import org.springframework.idusmartii.friends.FriendState;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchRepository;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@WebMvcTest(controllers = BoardController.class, 
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
public class BoardControllerTests {
	private  Integer TEST_BOARD_ID = 100;
	
	
	@Autowired
	private BoardController boardcontroller;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BoardService boardService;
	
	@MockBean
	private MatchRepository matchRepository;
	
	@MockBean
	private PlayerService playerService;
	

	
	private Board boardTest;
	

	
	@MockBean
	private AuthoritiesService authoritiesService;
	@MockBean
	private UserService userService;
	
	@MockBean
	private FriendService friendService;
	
	
	@BeforeEach
	void setup() throws Exception {
		List<Player> players = auxCreateplayers(8);
		Player espectator = players.get(6);
		Player p5 = players.get(5);
		
        Friend friend = new Friend();
        friend.setId(100);
        friend.setPlayer1(players.get(0));
        friend.setPlayer2(players.get(6));
        friend.setFriendState(FriendState.ACCEPTED);
        friend.setSender(players.get(0));
        friend.setReceiver(players.get(6));
        
        
        List<Friend> f = new ArrayList<Friend>();
        f.add(friend);
        Collection<Friend> f2 = f;
        
        players.remove(espectator);
        players.remove(p5);
        
        boardTest = new Board(players.size());
		boardTest.setAnfitrion(players.get(0));
		boardTest.setId(TEST_BOARD_ID);
		boardTest.setPlayingMatch(null);
		Optional<Board> b = Optional.of(boardTest);
		
		Match match = new Match(players);
		match.setId(101);
		Board board = new Board(players);
		board.setAnfitrion(players.get(0));
		board.setId(101);
		board.setPlayingMatch(match);
		
		players.get(0).setBoard(boardTest);
		
		/*
		 * Para el test de showBoardSuccesful
		 */
        given(this.playerService.getByUsername("testUser0")).willReturn(players.get(0));
        given(this.playerService.findPlayerByBoardId(100)).willReturn(players);
        given(this.boardService.findById(boardTest.getId())).willReturn(b);
        given(this.friendService.findFriendOfPlayer(players.get(0))).willReturn(f2);
        
        /*
         * Para el test de espectador
         */
        given(this.playerService.getByUsername("testUser6")).willReturn(espectator);
        given(this.boardService.findById(101)).willReturn(Optional.of(board));
        given(this.playerService.findPlayerByBoardId(101)).willReturn(players);
        
        given(this.friendService.findFriendById(100)).willReturn(Optional.of(friend));
	}
	
	
	@WithMockUser(username = "testUser6", authorities = {"PLAYER"})
    @Test
	void joinToABoardSuccesful() throws Exception {
		mockMvc.perform(get("/play/join/{boardId}",100)).andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(username = "testUser0", authorities = {"PLAYER"})
		@Test
	void showBoardSuccesful() throws Exception {
		mockMvc.perform(get("/board/{boardId}", 100)).andExpect(status().isOk())
		.andExpect(view().name("boards/board"))
		.andExpect(model().attributeExists("board"))
        .andExpect(model().attributeExists("players"))
        .andExpect(model().attributeExists("player"));
	}	
	
	@WithMockUser(username = "testUser6", authorities = {"PLAYER"})
	@Test
	void watchBoardSuccesful() throws Exception {
		mockMvc.perform(get("/board/watch/{boardId}", 101)).andExpect(status().isOk())
		.andExpect(view().name("boards/board"))
        .andExpect(model().attributeExists("watching"));
	}	
	
	@WithMockUser(username = "testUser6", authorities = {"PLAYER"})
	@Test
	void boardsSuccesful() throws Exception {
		mockMvc.perform(get("/play")).andExpect(status().isOk())
		.andExpect(view().name("/play/boardsList"))
		.andExpect(model().attributeExists("boards"));
	}
	
	@WithMockUser(username = "testUser6", authorities = {"PLAYER"})
	@Test
	void playNewSuccesful() throws Exception {
		mockMvc.perform(get("/play/new")).andExpect(status().is3xxRedirection());
	}
	

	

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


	
	
}
