package org.springframework.idusmartii.pretor;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.idusmartii.board.BoardService;
import org.springframework.idusmartii.configuration.SecurityConfiguration;
import org.springframework.idusmartii.consul.Consul;
import org.springframework.idusmartii.consul.ConsulController;
import org.springframework.idusmartii.edil.Edil;
import org.springframework.idusmartii.edil.EdilService;
import org.springframework.idusmartii.edil.VOTE;
import org.springframework.idusmartii.faction.FactionService;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchService;
import org.springframework.idusmartii.match.MatchTurnStatus;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.turn.Turn;
import org.springframework.idusmartii.turn.TurnService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = PretorController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
    excludeAutoConfiguration = SecurityConfiguration.class)
public class PretorControllerTest {


    private static final Integer TEST_PRETOR_ID =100;
    private static final Integer TEST_EDIL_ID =101;
    private static final Integer TEST_MATCH_ID = 100;
    private  Player TEST_PLAYER_PRETOR;
    private  Player TEST_PLAYER_EDIL;
    private Match TEST_MATCH;
    private Pretor TEST_PRETOR;
    private Edil TEST_EDIl;
    private Turn TEST_TURN;



    @Autowired
    private PretorController pretorController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private TurnService turnService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private FactionService factionService;

    @MockBean
    private EdilService edilService;

    @MockBean
    private PretorService pretorService;

    @BeforeEach
    void setup() throws Exception{
        Player pretor = new Player();
        pretor.setId(this.TEST_PRETOR_ID);
        this.TEST_PLAYER_PRETOR = pretor;
        Player edil = new Player();
        edil.setId(this.TEST_EDIL_ID);
        this.TEST_PLAYER_EDIL = edil;

        Pretor pretorTest = new Pretor(pretor);
        this.TEST_PRETOR = pretorTest;
        Match matchTest = new Match(5);
        matchTest.setId(this.TEST_MATCH_ID);

        Turn turnTest = new Turn(matchTest, 1);
        this.TEST_TURN = turnTest;
        Edil edilTest = new Edil(turnTest, edil);
        edilTest.setVote(VOTE.LOYAL);
        pretorTest.setEdil(edilTest);
        turnTest.setMatchTurnStatus(MatchTurnStatus.CHANGING_VOTE);
        turnTest.setPlayerEdil(Lists.newArrayList(edilTest,new Edil(turnTest, new Player())));
        turnTest.setPretor(pretorTest);
        this.TEST_EDIl = edilTest;

        matchTest.setTurnList(Lists.newArrayList(turnTest));

        given(this.playerService.findPlayerById(TEST_PRETOR_ID)).willReturn(Optional.of(TEST_PLAYER_PRETOR));
        given(this.playerService.findPlayerById(TEST_EDIL_ID)).willReturn(Optional.of(TEST_PLAYER_EDIL));
        given(this.matchService.findMatchById(this.TEST_MATCH_ID)).willReturn(Optional.of(matchTest));
        given(this.playerService.getByUsername("spring")).willReturn(this.TEST_PLAYER_PRETOR);
        given(this.edilService.findById(TEST_EDIL_ID)).willReturn(Optional.of(edilTest));
    }



    @WithMockUser(value = "spring")
    @Test
    void tetEditEdilVoteLoyal() throws Exception{
        mockMvc.perform(get("/match/{matchId}/action/pretor/changeVote/{edilId}", TEST_MATCH_ID, TEST_EDIL_ID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/match/"+TEST_MATCH_ID));
    }
    @WithMockUser(value = "spring")
    @Test
    void tetEditEdilVoteTraitor() throws Exception{
        this.TEST_EDIl.setVote(VOTE.TRAITOR);
        mockMvc.perform(get("/match/{matchId}/action/pretor/changeVote/{edilId}", TEST_MATCH_ID, TEST_EDIL_ID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/match/"+TEST_MATCH_ID));
    }


    @WithMockUser(value = "spring")
    @Test
    void tetEditEdilVote2Round() throws Exception{
        this.TEST_TURN.setTurn(12);
        mockMvc.perform(get("/match/{matchId}/action/pretor/changeVote/{edilId}", TEST_MATCH_ID, TEST_EDIL_ID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/match/"+TEST_MATCH_ID));
    }

    @WithMockUser(value = "spring")
    @Test
    void tetEditEdilVoteNotPretorEdil() throws Exception{
        this.TEST_PRETOR.setEdil(new Edil(this.TEST_TURN, new Player()));
        mockMvc.perform(get("/match/{matchId}/action/pretor/changeVote/{edilId}", TEST_MATCH_ID, TEST_EDIL_ID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/match/"+TEST_MATCH_ID));
    }
}
