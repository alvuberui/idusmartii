package org.springframework.idusmartii.players;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.idusmartii.achievement.AchivementService;
import org.springframework.idusmartii.board.BoardService;
import org.springframework.idusmartii.configuration.SecurityConfiguration;
import org.springframework.idusmartii.estadistics.Estadistics;
import org.springframework.idusmartii.estadistics.EstadisticsService;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlayerController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = WebSecurityConfigurer.class),
    excludeAutoConfiguration = SecurityConfiguration.class)

public class PlayerControllerTest {

    private static final Integer TEST_PLAYER_ID = 100;
    private static final Integer TEST_MATCH_ID = 100;


    @Autowired
    private PlayerController playerController;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private UserService userService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private EstadisticsService estadisticsService;

    @MockBean
    private AchivementService achivementService;

    @MockBean
    private AuthoritiesService authoritiesService;

    @Autowired
    private MockMvc mockMvc;

    private Player playerTest;
    private Match matchTest;


    @BeforeEach
    void setup(){
        User userTest = new User();
        userTest.setUsername("Player_Test");
        userTest.setPassword("1235");
        userTest.setEnabled(Boolean.TRUE);



        Estadistics e = new Estadistics();
        e.setAchivementList(Lists.newArrayList());

        playerTest = new Player();
        e.setPlayer(playerTest);
        playerTest.setId(TEST_PLAYER_ID);
        playerTest.setUser(userTest);
        playerTest.setEmail("PlayerTest@email.com");
        playerTest.setFirstName("Player");
        playerTest.setLastName("Test");
        playerTest.setEstadistic(e);

        authoritiesService.saveAuthorities(playerTest.getUser().getUsername(),"player");
        authoritiesService.saveAuthorities(playerTest.getUser().getUsername(),"admin");

        matchTest = new Match();
        matchTest.setId(TEST_MATCH_ID);

        given(this.playerService.getByUsername("spring")).willReturn(this.playerTest);
        given(this.playerService.findPlayerById(TEST_PLAYER_ID)).willReturn(java.util.Optional.ofNullable(playerTest));
        given(this.playerService.findPlayerByMatchId(TEST_MATCH_ID)).willReturn(Lists.newArrayList());
        given(this.playerService.getByUsername(playerTest.getUser().getUsername())).willReturn(this.playerTest);
    }

    // TESTEADO
    @WithMockUser(value = "spring")
    @Test
    void testPlayerList() throws Exception{

        Pageable page = PageRequest.of(0, 3);
        Page<Player> pages = new PageImpl<>(Lists.newArrayList(playerTest));
        given(this.playerService.getPage(page)).willReturn(pages);
        mockMvc.perform(get("/admin/playersList")).andExpect(status().isOk())
            .andExpect(view().name("players/playerList"))
            .andExpect(model().attributeExists("number"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("totalElements"))
            .andExpect(model().attributeExists("size"))
            .andExpect(model().attributeExists("data"))
            .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    void testCreatePlayerGet() throws Exception{
        mockMvc.perform(get("/admin/playersList/createPlayer"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("player"))
            .andExpect(view().name("players/createPlayer"));
    }
    //TESTEADO
    @WithMockUser(value = "spring")
    @Test
    void testCreatePlayerPost() throws Exception{
        mockMvc.perform(post("/admin/playersList/createPlayer")
            .param("username", "Player_Test")
            .param("password", "12356")
            .param("firstName", "PlayerTest1")
            .param("lastName", "Test1").with(csrf())
            .param("email", "PlayerTest1@email.com")
        ).andExpect(view().name("redirect:/admin/playersList"));
    }

 

    //TESTEADO
    @WithMockUser(value = "spring")
    @Test
    void testUpdatePlayerGetSuccessful() throws Exception{
        mockMvc.perform(get("/admin/playersList/update/{playerId}",TEST_PLAYER_ID))
            .andExpect(model().attributeExists("player"))
            .andExpect(model().attributeExists("oldplayer"))
            .andExpect(status().isOk())
            .andExpect(view().name("players/updatePlayer"));
    }



    //TESTEADO
    @WithMockUser(value = "spring")
    @Test
    void testUpdatePlayerGetFailed() throws Exception{
        mockMvc.perform(get("/admin/playersList/update/{playerId}",77))
            .andExpect(model().attributeExists("player"))
            .andExpect(model().attributeExists("message"))
            .andExpect(model().attributeDoesNotExist("oldplayer"))
            .andExpect(status().isOk())
            .andExpect(view().name("players/updatePlayer"));
    }

  

    //TESTEADO
    @WithMockUser(value = "spring")
    @Test
    void testDeletedPlayerSuccessful() throws Exception{
        mockMvc.perform(get("/admin/playerList/delete/{playerId}", TEST_PLAYER_ID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/admin/playersList"));
    }


    @WithMockUser(value = "spring")
    @Test
    void testDeletedPlayerFailed() throws Exception{
        mockMvc.perform(get("/admin/playerList/delete/{playerId}", 10000))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/admin/playersList"));
    }


    // TESTEADO
    @WithMockUser(value = "spring")
    @Test
    void testPlayerEditGetSuccessful() throws Exception{
        mockMvc.perform(get("/player/edit"))
            .andExpect(status().isOk())
            .andExpect(view().name("users/createPlayerForm"))
            .andExpect(model().attribute("player", playerTest));
    }





    //TESTEADO
    @WithMockUser(value = "spring")
    @Test
    void testFindPlayerSuccessful() throws Exception{
        given(this.playerService.findByUsernameContaining("a")).willReturn(Lists.newArrayList(playerTest));
        mockMvc.perform(get("/findPlayer?username=a"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("user"))
            .andExpect(view().name("players/findPlayer"))
            .andExpect(status().isOk());
    }


    //TESTEADO
    @WithMockUser(value = "spring")
    @Test
    void testFindPlayerLength0() throws Exception{
        given(this.playerService.findByUsernameContaining("")).willReturn(Lists.newArrayList());
        mockMvc.perform(get("/findPlayer?username="))
            .andExpect(model().attributeExists("message"))
            .andExpect(view().name("players/findPlayer"))
            .andExpect(status().isOk());
    }


    //TESTEADO
    @WithMockUser(value = "spring")
    @Test
    void testFindPlayerFail() throws Exception{
        given(this.playerService.findByUsernameContaining("x")).willReturn(Lists.newArrayList());
        mockMvc.perform(get("/findPlayer?username=x"))
            .andExpect(model().attributeExists("message"))
            .andExpect(view().name("players/findPlayer"))
            .andExpect(status().isOk());
    }

   


    @WithMockUser(value = "spring")
    @Test
    void testListPlayerByMatch() throws Exception {
        given(this.playerService.findPlayerByMatchId(100)).willReturn(Lists.newArrayList());
        mockMvc.perform(get("/playerList/match/100"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("players"))
            .andExpect(view().name("matchs/match"));
    }

}
