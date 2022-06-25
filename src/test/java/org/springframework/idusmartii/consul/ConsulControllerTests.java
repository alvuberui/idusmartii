package org.springframework.idusmartii.consul;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import org.springframework.idusmartii.edil.Edil;
import org.springframework.idusmartii.pretor.Pretor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.idusmartii.configuration.SecurityConfiguration;
import org.springframework.idusmartii.edil.EdilService;
import org.springframework.idusmartii.faction.Faction;
import org.springframework.idusmartii.faction.FactionCard;
import org.springframework.idusmartii.faction.FactionService;
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
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.idusmartii.board.BoardControllerTests;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ConsulController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
    excludeAutoConfiguration = SecurityConfiguration.class)

public class ConsulControllerTests {

    private static final Integer TEST_CONSUL_ID = 100;
    private static final Integer TEST_MATCH_ID = 100;
    private  Player TEST_PLAYER;
    private Match TEST_MATCH;
    private Consul TEST_CONSUL;

    @Autowired
    private ConsulController consulController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsulService consulService;

    @MockBean
    private MatchService matchService;

    @MockBean
    private FactionService factionService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private TurnService turnService;

    @MockBean
    private EdilService edilService;

    @MockBean
    private AuthoritiesService authoritiesService;
    @MockBean
    private UserService userService;


    @BeforeEach
    void setup() throws Exception{

        List<Player> players = auxCreateplayers(5);
        Player player = players.get(0);
        player.setId(this.TEST_CONSUL_ID);
        this.TEST_PLAYER = player;

        Consul consulTest = new Consul(player);
        consulTest.setId(this.TEST_CONSUL_ID);
        this.TEST_CONSUL = consulTest;
        Match matchTest = new Match(players);
        matchTest.setId(this.TEST_MATCH_ID);

        Turn turnTest = new Turn(matchTest, 1);
        turnTest.setConsul(consulTest);
        turnTest.setMatchTurnStatus(MatchTurnStatus.CHOOSE_FACCTION);
        turnTest.setPlayerEdil(Lists.newArrayList(new Edil(turnTest, players.get(2)),new Edil(turnTest, players.get(3))));
        turnTest.setPretor(new Pretor(players.get(1)));
        matchTest.setTurnList(Lists.newArrayList(turnTest));
        this.TEST_MATCH = matchTest;



        FactionCard factionCard1 = new FactionCard(player, matchTest, Faction.LOYAL);
        FactionCard factionCard2 = new FactionCard(player, matchTest, Faction.TRAITOR);
        FactionCard factionCard3 = new FactionCard(player, matchTest, Faction.MERCHANT);

        //Collection<FactionCard> cardList = Lists.newArrayList(factionCard1, factionCard2, factionCard3);
        Collection<FactionCard> cardList = Lists.newArrayList(factionCard2, factionCard3);

        given(this.playerService.findPlayerById(TEST_CONSUL_ID)).willReturn(Optional.of(TEST_PLAYER));
        given(this.matchService.findMatchById(this.TEST_MATCH_ID)).willReturn(Optional.of(matchTest));
        given(this.factionService.findByMatchAndPlayer(this.TEST_MATCH, this.TEST_PLAYER)).willReturn(cardList);
        given(this.playerService.getByUsername("spring")).willReturn(this.TEST_PLAYER);


    }


    @WithMockUser(value = "spring")
    @Test
    void chooseLoyalFactionSuccesful() throws Exception {
        mockMvc.perform(get("/match/"+this.TEST_MATCH_ID+"/action/consul/chooseFaction/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/match/"+this.TEST_MATCH_ID));
    }

    @WithMockUser(value = "spring")
    @Test
    void chooseTraitorFactionSuccesful() throws Exception {
        mockMvc.perform(get("/match/"+this.TEST_MATCH_ID+"/action/consul/chooseFaction/2"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/match/"+this.TEST_MATCH_ID));
    }

    @WithMockUser(value = "spring")
    @Test
    void chooseMerchantFactionSuccesful() throws Exception {
        mockMvc.perform(get("/match/"+this.TEST_MATCH_ID+"/action/consul/chooseFaction/3"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/match/"+this.TEST_MATCH_ID));
    }



    @WithMockUser(value = "spring")
    @Test
    void testChooseRolePretor() throws Exception {
        Turn turnTest2 = new Turn(this.TEST_MATCH, 2);
        turnTest2.setConsul(this.TEST_CONSUL);
        turnTest2.setMatchTurnStatus(MatchTurnStatus.CHOOSE_ROL);
        this.TEST_MATCH.getTurnList().add(turnTest2);
        mockMvc.perform(get("/match/{matchId}/action/consul/chooseRol/{playerId}/1", TEST_MATCH_ID, TEST_CONSUL_ID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/match/"+this.TEST_MATCH_ID));
    }


//    @WithMockUser(value = "spring")
//    @Test
//    void testChooseRoleEdil() throws Exception {
//        Turn turnTest2 = new Turn(this.TEST_MATCH, 2);
//        turnTest2.setConsul(this.TEST_CONSUL);
//        turnTest2.setMatchTurnStatus(MatchTurnStatus.CHOOSE_ROL);
//        turnTest2.setPlayerEdil(Lists.newArrayList());
//        turnTest2.setPretor(new Pretor());
//        mockMvc.perform(get("/match/{matchId}/action/consul/chooseRol/{playerId}/2", TEST_MATCH_ID, TEST_CONSUL_ID, 2))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(view().name("redirect:/match/"+this.TEST_MATCH_ID));
//    }




    public  List<Player> auxCreateplayers(Integer numPlayers) throws Exception {
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
                playerService.savePlayer(testPlayer);
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
