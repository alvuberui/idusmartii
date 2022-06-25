package org.springframework.idusmartii.estadistics;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.idusmartii.achievement.AchivementService;
import org.springframework.idusmartii.configuration.SecurityConfiguration;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchService;
import org.springframework.idusmartii.players.IteratorToStream;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = EstadisticsController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

public class EstadisticsControllerTest {

	private static final Integer TEST_PLAYER_ID = 200;
	
	@Autowired
	private MockMvc mockMvc;
	
    @MockBean
    private EstadisticsService estadisticsService;
	
    @MockBean
    private AchivementService achivementService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private UserService userService;
    
    @MockBean
    private AuthoritiesService authoritiesService;
    
    @MockBean
    private MatchService matchService;
    
    @BeforeEach
    void setup(){
        User userTest = new User();
        userTest.setUsername("Player_Test");
        userTest.setPassword("1235");
        userTest.setEnabled(Boolean.TRUE);

        Estadistics e = new Estadistics();
        e.setMatchLongerDuration(5000);
        e.setMatchShorterDuration(1000);
        e.setAchivementList(Lists.newArrayList());

        Player playerTest = new Player();
        e.setPlayer(playerTest);
        playerTest.setId(TEST_PLAYER_ID);
        playerTest.setUser(userTest);
        playerTest.setEmail("PlayerTest@email.com");
        playerTest.setFirstName("Player");
        playerTest.setLastName("Test");
        playerTest.setEstadistic(e);

        authoritiesService.saveAuthorities(playerTest.getUser().getUsername(),"player");
        authoritiesService.saveAuthorities(playerTest.getUser().getUsername(),"admin");

        List<Player> list = new ArrayList<>();
        list.add(playerTest);
        
        
        given(this.playerService.getByUsername("spring")).willReturn(playerTest);
        given(this.playerService.findPlayerById(TEST_PLAYER_ID)).willReturn(java.util.Optional.ofNullable(playerTest));
        given(this.playerService.getByUsername(playerTest.getUser().getUsername())).willReturn(playerTest);
     	given(this.playerService.findAll()).willReturn(list);
    }
    
    @WithMockUser(value = "spring")
    @Test
    void testGlobalEstadistics() throws Exception{
    	mockMvc.perform(get("/globalEstadistics"))
    		.andExpect(model().attributeExists("globalEstadistic"))
    		.andExpect(model().attributeExists("playerRanking"))
    		.andExpect(model().attributeExists("totalPlayers"))
    		.andExpect(model().attributeExists("actualPlayer"))
    		.andExpect(model().attributeExists("shortest"))
    		.andExpect(model().attributeExists("longuest"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("estadistics/globalEstadistics"));
    		
    }
	
	
}
