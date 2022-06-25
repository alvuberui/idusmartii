package org.springframework.idusmartii.friend;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.idusmartii.achievement.AchivementService;
import org.springframework.idusmartii.board.Board;
import org.springframework.idusmartii.comment.CommentService;
import org.springframework.idusmartii.configuration.SecurityConfiguration;
import org.springframework.idusmartii.consul.ConsulService;
import org.springframework.idusmartii.edil.EdilService;
import org.springframework.idusmartii.estadistics.EstadisticsService;
import org.springframework.idusmartii.faction.FactionService;
import org.springframework.idusmartii.friends.Friend;
import org.springframework.idusmartii.friends.FriendController;
import org.springframework.idusmartii.friends.FriendRepository;
import org.springframework.idusmartii.friends.FriendService;
import org.springframework.idusmartii.friends.FriendState;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchController;
import org.springframework.idusmartii.match.MatchRepository;
import org.springframework.idusmartii.match.MatchService;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerController;
import org.springframework.idusmartii.players.PlayerRepository;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.pretor.PretorService;
import org.springframework.idusmartii.turn.TurnService;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserController;
import org.springframework.idusmartii.user.UserService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;



//@ExtendWith( SpringExtension.class)
//@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext
@WebMvcTest(controllers = FriendController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
classes = WebSecurityConfigurer.class),
excludeAutoConfiguration = SecurityConfiguration.class)
public class FriendControllerTest {

	 private static final Integer TEST_PLAYER1_ID = 101;
	 private static final Integer TEST_PLAYER2_ID = 102;
	 private static final Integer TEST_PLAYER3_ID = 103;
	 private static final Integer TEST_PLAYER4_ID = 104;
	 private static final Integer TEST_FRIEND_ID = 100;

	 public Integer TEST_PLAYER1p_ID = 101;
	 public Integer TEST_PLAYER2p_ID = 102;
	 public Integer TEST_PLAYER3p_ID = 103;
	 public Integer TEST_PLAYER4p_ID = 104;


   @Autowired
   private FriendController FriendController;

	@MockBean
	private FriendService friendService;

    @MockBean
    private PlayerService playerService;



    @MockBean
    private UserService userService;

    @MockBean
    private AuthoritiesService authoritiesService;

    @MockBean
    private JavaMailSender mailSender;


    @Autowired
    private MockMvc mockMvc;



    @Autowired
    private WebApplicationContext context;


    private Player playerTest1;
    private Player playerTest2;
    private Player playerTest3;
    private Player playerTest4;
    private Friend friendTest;
    private Authentication auth;

    @BeforeEach
    void setup() throws Exception{

    	List<Player> playersF = auxCreateplayersForFriendsC(5);

    	playerTest1=playersF.get(0);
    	playerTest2=playersF.get(1);
    	playerTest3=playersF.get(2);
    	playerTest4=playersF.get(3);
		friendTest = new Friend();
		friendTest.setId(TEST_FRIEND_ID);
		friendTest.setPlayer1(playerTest1);
		friendTest.setPlayer2(playerTest2);
		friendTest.setFriendState(FriendState.PENDING);
		TEST_PLAYER1p_ID=playerTest1.getId();
		TEST_PLAYER2p_ID=playerTest2.getId();
		TEST_PLAYER3p_ID=playerTest3.getId();
		TEST_PLAYER4p_ID=playerTest4.getId();




        try {
            this.friendService.saveFriend(friendTest);
        } catch (Exception ex) {
            System.out.println("El amigo no ha podido ser guardada");
        }



    }



    @WithMockUser(username = "marpercor8", authorities = {"PLAYER"})
    @Test
	void friendlistSuccesful() throws Exception {
		mockMvc.perform(get("/friendList")).andExpect(status().isOk());
	}


    @WithMockUser(username = "marpercor8", authorities = {"PLAYER"})
    @Test
	void friendSuccesfulDelete() throws Exception {
		mockMvc.perform(get("/friendList/delete/{friendId}",TEST_FRIEND_ID))
		.andExpect(status().isMovedTemporarily())
		.andExpect(view().name("redirect:/friendList"));
	}





    @WithMockUser(username = "marpercor8", authorities = {"PLAYER"})
    @Test
	void friendSuccesfulCreate() throws Exception {
		mockMvc.perform(get("friendList/new/{playerId}",playerTest3.getId()))
		.andExpect(status().isNotFound());

	}


    @WithMockUser(username = "marpercor8", authorities = {"PLAYER"})
    @Test
	void friendSuccesfulEdit() throws Exception {

        given(friendService.findFriendById(TEST_FRIEND_ID)).willReturn(Optional.empty());
		mockMvc.perform(get("/friendList/edit/{friendId}",TEST_FRIEND_ID))
		.andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/friendList"));
        Assert.assertEquals(FriendState.PENDING, friendTest.getFriendState());
        given(friendService.findFriendById(TEST_FRIEND_ID)).willReturn(Optional.of(friendTest));
        mockMvc.perform(get("/friendList/edit/{friendId}",TEST_FRIEND_ID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/friendList"));
        Assert.assertEquals(FriendState.ACCEPTED, friendTest.getFriendState());
	}




    @Transactional
	public List<Player> auxCreateplayersForFriendsC(Integer numPlayers) throws Exception {
		List<Player> players = new ArrayList<Player>();

		for(int acum=0; acum < numPlayers; acum++) {
			Player testPlayer = new Player();
			testPlayer.setFirstName("pruebaFriendC"+acum);
			testPlayer.setLastName("testFriendC"+acum);
			testPlayer.setEmail("pruebaFriendC"+acum+"@gmail.com");
			testPlayer.setId(1000+acum);
			testPlayer.setUser(null);

			User testUser = new User();
		    testUser.setUsername("testUserFriendC"+acum);
		    testUser.setPassword("supersecretpasswordFriendC"+acum);
		    testUser.setEnabled(true);
			testUser.setUsername("testUserFriendC"+acum);

			testPlayer.setUser(testUser);

			try {
				this.playerService.savePlayer(testPlayer);
			} catch (Exception CantSavePlayer) {
				throw new Exception("Hay un problema al guardar el player");
			}

			try {
				authoritiesService.saveAuthorities("testUserFriendC"+acum,"player");
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
