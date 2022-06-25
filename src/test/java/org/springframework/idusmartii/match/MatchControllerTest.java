package org.springframework.idusmartii.match;


import org.assertj.core.api.OptionalLongAssert;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.idusmartii.achievement.AchivementService;
import org.springframework.idusmartii.board.Board;
import org.springframework.idusmartii.board.BoardService;
import org.springframework.idusmartii.comment.Comment;
import org.springframework.idusmartii.comment.CommentRepository;
import org.springframework.idusmartii.comment.CommentService;
import org.springframework.idusmartii.configuration.SecurityConfiguration;
import org.springframework.idusmartii.consul.Consul;
import org.springframework.idusmartii.consul.ConsulRepository;
import org.springframework.idusmartii.consul.ConsulService;
import org.springframework.idusmartii.edil.Edil;
import org.springframework.idusmartii.edil.EdilRepository;
import org.springframework.idusmartii.edil.EdilService;
import org.springframework.idusmartii.edil.VOTE;
import org.springframework.idusmartii.estadistics.Estadistics;
import org.springframework.idusmartii.estadistics.EstadisticsService;
import org.springframework.idusmartii.faction.Faction;
import org.springframework.idusmartii.faction.FactionCard;
import org.springframework.idusmartii.faction.FactionRepository;
import org.springframework.idusmartii.faction.FactionService;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerController;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.pretor.Pretor;
import org.springframework.idusmartii.pretor.PretorRepository;
import org.springframework.idusmartii.pretor.PretorService;
import org.springframework.idusmartii.turn.Turn;
import org.springframework.idusmartii.turn.TurnRepository;
import org.springframework.idusmartii.turn.TurnService;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MatchController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
    classes = WebSecurityConfigurer.class),
    excludeAutoConfiguration = SecurityConfiguration.class)
@MockBeans({
    @MockBean(MatchService.class),
    @MockBean(FactionService.class),
    @MockBean(CommentService.class),
    @MockBean(EdilService.class),
    @MockBean(PretorService.class),
    @MockBean(TurnService.class),
    @MockBean(ConsulService.class),
    @MockBean(EstadisticsService.class),
    @MockBean(AchivementService.class)
})

public class MatchControllerTest {


    @Autowired
    private MatchController matchController;

    @MockBean
    private MatchService matchService;

    @MockBean
    private TurnService turnService;

    @MockBean
    private ConsulService consulService;

    @MockBean
    private MatchRepository matchRepository;

    @MockBean
    private AuthoritiesService authoritiesService;

    @MockBean
    private UserService userService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private BoardService boardService;


    @MockBean
    private FactionService factionService;

    @MockBean
    private CommentService commentService;



    @Autowired
    private MockMvc mockMvc;


    private static final Integer TEST_PLAYER_ID = 100;
    private static final Integer TEST_MATCH_ID = 100;
    private static final Integer TEST_BOARD_ID = 100;


    private static final String USERNAME_TEST = "USER_TEST";


    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);

    // Variables globales
    private Board boardTest;

    private List<Player> players;

    private Match matchTest;
    private Turn turnTest;
    private Edil edilTest1;
    private Edil edilTest2;
    private Pretor pretorTest;
    private Consul consulTest;


    private Integer ANFITRION_INDEX = 0;
    private Integer CONSUL_INDEX = 0;
    private Integer PRETOR_INDEX = 1;
    private Integer EDIL1_INDEX = 2;
    private Integer EDIL2_INDEX = 3;
    private Integer PLAYER_INDEX = 4;


    public List<Player> auxCreateplayers(Integer numPlayers) throws Exception {
        List<Player> players = new ArrayList<Player>();

        for(int acum=0; acum < numPlayers; acum++) {
            Player testPlayer = new Player();
            testPlayer.setFirstName("prueba"+acum);
            testPlayer.setLastName("test"+acum);
            testPlayer.setEmail("prueba"+acum+"@gmail.com");
            testPlayer.setUser(null);
            testPlayer.setId(acum+1);
            testPlayer.setEstadistic(new Estadistics());

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

    Match generateMatch(){
        Player playerAnfitrion = players.get(ANFITRION_INDEX);

        Player playerPlayer = players.get(PLAYER_INDEX);


        boardTest.setAnfitrion(playerAnfitrion);
        given(this.playerService.getByUsername(USERNAME_TEST)).willReturn(playerAnfitrion);

        matchTest = new Match(players);
        matchTest.setId(100);
        matchTest.setMatchStatus(MatchStatus.PLAYING);
        matchTest.setBoard(boardTest);

        List<Turn> listTurn = new ArrayList<>();



        given(this.turnService.findTurnByMatch(matchTest)).willReturn(listTurn);
        for(Player player : players){

            FactionCard factionCard1_test = new FactionCard(player, matchTest, Faction.LOYAL);
            FactionCard factionCard2_test = new FactionCard(player, matchTest, Faction.LOYAL);
            List<FactionCard> factionCardList = List.of(factionCard1_test, factionCard2_test);

            given(this.factionService.findByMatchAndPlayer(matchTest, player)).willReturn(factionCardList);
        }

        given(this.matchService.findMatchById(100)).willReturn(Optional.of(matchTest));


        return matchTest;
    }

    Turn generateTurn(){
        Player playerConsul = players.get(CONSUL_INDEX);
        Player playerPretor = players.get(PRETOR_INDEX);
        Player playerEdil1 = players.get(EDIL1_INDEX);
        Player playerEdil2 = players.get(EDIL2_INDEX);
        consulTest = new Consul(playerConsul);
        pretorTest = new Pretor(playerPretor);
        FactionCard factionCard1_test = new FactionCard(playerConsul, matchTest, Faction.LOYAL);
        consulTest.setFaction(factionCard1_test);


        turnTest = new Turn(matchTest, 0, consulTest);
        edilTest1 = new Edil(turnTest, playerEdil1);
        edilTest2 = new Edil(turnTest, playerEdil2);
        List<Edil> edilList = List.of(edilTest1, edilTest2);
        turnTest.setPlayerEdil(edilList);
        turnTest.setPretor(pretorTest);

        turnTest.setMatchTurnStatus(MatchTurnStatus.CONT);
        turnTest.setNextStateTurn(LocalTime.now().plusSeconds(10));


        matchTest.setTurnList(List.of(turnTest));
        given(this.consulService.getConsulByPlayer(boardTest.getAnfitrion(), matchTest)).willReturn(Optional.of(consulTest));
        given(this.turnService.findTurnByMatch(matchTest)).willReturn(List.of(turnTest));
        return turnTest;
    }


    void generateTurns(Integer i){
        Player playerConsul = players.get(CONSUL_INDEX);
        consulTest = new Consul(playerConsul);
        List<Turn> turnList = new ArrayList<>();

        Player playerPretor = players.get(PRETOR_INDEX);
        Player playerEdil1 = players.get(EDIL1_INDEX);
        Player playerEdil2 = players.get(EDIL2_INDEX);
        pretorTest = new Pretor(playerPretor);


        turnList.addAll(matchTest.getTurnList());
        for(int a = 0; a<i; a++){
            Turn turnTest1 = new Turn(matchTest, 0, consulTest);
            edilTest1 = new Edil(turnTest, playerEdil1);
            edilTest2 = new Edil(turnTest, playerEdil2);
            List<Edil> edilList = List.of(edilTest1, edilTest2);
            turnTest1.setPlayerEdil(edilList);
            turnTest1.setPretor(pretorTest);

            turnTest1.setMatchTurnStatus(MatchTurnStatus.CONT);
            turnTest1.setNextStateTurn(LocalTime.now().plusSeconds(10));


            turnTest1.setTurn(a);
            turnList.add(turnTest1);
        }

        given(this.turnService.findTurnByMatch(matchTest)).willReturn(turnList);
    }


    @BeforeEach
    void setup() throws Exception {
        boardTest = new Board(5);
        boardTest.setId(100);
        boardService.saveBoard(boardTest);
        players = auxCreateplayers(5);


        given(this.boardService.findById(100)).willReturn(Optional.of(boardTest));
        given(this.playerService.findPlayerByBoardId(100)).willReturn(players);
        given(this.matchService.getMatchBoardId(boardTest)).willReturn(boardTest.getPlayingMatch());
    }

    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void testCreateMatch() throws Exception{
        mockMvc.perform(get("/match/new/100")).andExpect(status().is3xxRedirection());
    }

    //Passed
    @WithMockUser(username = "marpercor8", authorities = {"PLAYER"})
    @Test
    void testCreatedMatchWaiting() throws Exception{


        Player anfitrion = players.get(0);
        boardTest.setAnfitrion(anfitrion);
        given(this.playerService.getByUsername("marpercor8")).willReturn(anfitrion);

        Match match = new Match(players);
        match.setId(100);
        match.setMatchStatus(MatchStatus.WAITTING);
        match.setBoard(boardTest);


        List<Turn> listTurn = new ArrayList<>();

        given(this.matchService.findMatchById(100)).willReturn(Optional.of(match));
        given(this.turnService.findTurnByMatch(match)).willReturn(listTurn);

        mockMvc.perform(get("/match/100"))
            .andExpect(status().isOk())
            .andExpect(view().name("matchs/match"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("playerCredentials"))
            .andExpect(model().attributeExists("match"));

    }


    void generateMatch2(Match match, Turn turn){
        Player playerAnfitrion = players.get(ANFITRION_INDEX);
        Player playerConsul = players.get(CONSUL_INDEX);
        Player playerPretor = players.get(PRETOR_INDEX);
        Player playerEdil1 = players.get(EDIL1_INDEX);
        Player playerEdil2 = players.get(EDIL2_INDEX);
        Player playerPlayer = players.get(PLAYER_INDEX);


        boardTest.setAnfitrion(playerAnfitrion);
        given(this.playerService.getByUsername(USERNAME_TEST)).willReturn(playerAnfitrion);

        matchTest = new Match(players);
        matchTest.setId(100);
        matchTest.setMatchStatus(MatchStatus.PLAYING);
        matchTest.setBoard(boardTest);


        List<Turn> listTurn = new ArrayList<>();
        //Generamos los turnos
        consulTest = new Consul(playerConsul);
        pretorTest = new Pretor(playerPretor);

        FactionCard factionCard1_test = new FactionCard(playerPlayer, matchTest, Faction.LOYAL);
        FactionCard factionCard2_test = new FactionCard(playerPlayer, matchTest, Faction.LOYAL);



        List<FactionCard> factionCardList = List.of(factionCard1_test, factionCard2_test);
        turnTest = new Turn(matchTest, 0, consulTest);
        edilTest1 = new Edil(turnTest, playerEdil1);
        edilTest2 = new Edil(turnTest, playerEdil2);
        List<Edil> edilList = List.of(edilTest1, edilTest2);
        turnTest.setPlayerEdil(edilList);
        turnTest.setPretor(pretorTest);

        turnTest.setMatchTurnStatus(MatchTurnStatus.CONT);
        turnTest.setNextStateTurn(LocalTime.now().plusSeconds(10));
        listTurn.add(turnTest);

    }

    //Fail
    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void testCreateMatchPlayingNoTurnStart() throws Exception{
        Match m = generateMatch();
        matchTest = m;

        mockMvc.perform(get("/match/100"))
            .andExpect(status().isOk())
            .andExpect(view().name("matchs/match"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("playerCredentials"))
            .andExpect(model().attributeExists("match"));
    }

    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void testCreateMatchPlayingTurnStart() throws Exception{
        Match m = generateMatch();
        matchTest.setMatchStatus(MatchStatus.PLAYING);
        Turn turn = generateTurn();
        turnTest.getConsul().setFaction(null);

        mockMvc.perform(get("/match/100"))
            .andExpect(status().isOk())
            .andExpect(view().name("matchs/match"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("playerCredentials"))
            .andExpect(model().attributeExists("match"))
            .andExpect(model().attributeDoesNotExist("factionSelected"))
            .andExpect(model().attributeExists("turn"))
            .andExpect(model().attributeExists("round"));
    }
    //Passed
    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void testCreatedMatchConsulSelected() throws Exception{
        Match m = generateMatch();
        matchTest.setMatchStatus(MatchStatus.PLAYING);
        Turn turn = generateTurn();

        mockMvc.perform(get("/match/100"))
            .andExpect(status().isOk())
            .andExpect(view().name("matchs/match"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("playerCredentials"))
            .andExpect(model().attributeExists("match"))
            .andExpect(model().attributeExists("factionSelected"));
    }
    //Passed
    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void testCreatedMatchConsulNoSelected() throws Exception{
        Match m = generateMatch();
        matchTest.setMatchStatus(MatchStatus.PLAYING);
        Turn turn = generateTurn();
        turnTest.getConsul().setFaction(null);
        mockMvc.perform(get("/match/100"))
            .andExpect(status().isOk())
            .andExpect(view().name("matchs/match"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("playerCredentials"))
            .andExpect(model().attributeExists("match"))
            .andExpect(model().attributeDoesNotExist("factionSelected"));
    }

    //Passed
    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void testCreateMatchPlayingTurnStartedContWithVotes() throws Exception{

        Integer ANFITRION_INDEX = 0;
        Integer CONSUL_INDEX = 0;
        Integer PRETOR_INDEX = 1;
        Integer EDIL1_INDEX = 2;
        Integer EDIL2_INDEX = 3;
        Integer PLAYER_INDEX = 4;

        Player playerAnfitrion = players.get(ANFITRION_INDEX);
        Player playerConsul = players.get(CONSUL_INDEX);
        Player playerPretor = players.get(PRETOR_INDEX);
        Player playerEdil1 = players.get(EDIL1_INDEX);
        Player playerEdil2 = players.get(EDIL2_INDEX);
        Player playerPlayer = players.get(PLAYER_INDEX);


        boardTest.setAnfitrion(playerAnfitrion);
        given(this.playerService.getByUsername(USERNAME_TEST)).willReturn(playerAnfitrion);

        matchTest = new Match(players);
        matchTest.setId(100);
        matchTest.setMatchStatus(MatchStatus.PLAYING);
        matchTest.setBoard(boardTest);


        List<Turn> listTurn = new ArrayList<>();
        //Generamos los turnos
        consulTest = new Consul(playerConsul);
        pretorTest = new Pretor(playerPretor);

        FactionCard factionCard1_test = new FactionCard(playerPlayer, matchTest, Faction.LOYAL);
        FactionCard factionCard2_test = new FactionCard(playerPlayer, matchTest, Faction.LOYAL);



        List<FactionCard> factionCardList = List.of(factionCard1_test, factionCard2_test);
        turnTest = new Turn(matchTest, 0, consulTest);
        edilTest1 = new Edil(turnTest, playerEdil1);
        edilTest2 = new Edil(turnTest, playerEdil2);
        edilTest1.setVote(VOTE.LOYAL);
        edilTest2.setVote(VOTE.TRAITOR);
        List<Edil> edilList = List.of(edilTest1, edilTest2);
        turnTest.setPlayerEdil(edilList);
        turnTest.setPretor(pretorTest);

        turnTest.setMatchTurnStatus(MatchTurnStatus.CONT);
        turnTest.setNextStateTurn(LocalTime.now().plusSeconds(10));
        listTurn.add(turnTest);


        given(this.factionService.findByMatchAndPlayer(matchTest, playerAnfitrion)).willReturn(factionCardList);
        given(this.matchService.findMatchById(100)).willReturn(Optional.of(matchTest));
        given(this.turnService.findTurnByMatch(matchTest)).willReturn(listTurn);


        turnTest.setNextStateTurn(LocalTime.now().plusSeconds(10));

        Assert.assertEquals(Integer.valueOf(0), matchTest.getVotosTraidor());
        Assert.assertEquals(Integer.valueOf(0), matchTest.getVotosLeal());


        mockMvc.perform(get("/match/100"))
            .andExpect(status().isOk())
            .andExpect(view().name("matchs/match"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("playerCredentials"))
            .andExpect(model().attributeExists("match"))
            .andExpect(model().attributeDoesNotExist("factionSelected"))
            .andExpect(model().attributeExists("turn"))
            .andExpect(model().attributeExists("round"));

        Assert.assertEquals(Integer.valueOf(1), matchTest.getVotosTraidor());
        Assert.assertEquals(Integer.valueOf(1), matchTest.getVotosLeal());
    }
    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void testCreateMatchPlayingWaittovote() throws Exception{


        Integer ANFITRION_INDEX = 0;
        Integer CONSUL_INDEX = 0;
        Integer PRETOR_INDEX = 1;
        Integer EDIL1_INDEX = 2;
        Integer EDIL2_INDEX = 3;
        Integer PLAYER_INDEX = 4;

        Player playerAnfitrion = players.get(ANFITRION_INDEX);
        Player playerConsul = players.get(CONSUL_INDEX);
        Player playerPretor = players.get(PRETOR_INDEX);
        Player playerEdil1 = players.get(EDIL1_INDEX);
        Player playerEdil2 = players.get(EDIL2_INDEX);
        Player playerPlayer = players.get(PLAYER_INDEX);


        boardTest.setAnfitrion(playerAnfitrion);
        given(this.playerService.getByUsername(USERNAME_TEST)).willReturn(playerAnfitrion);

        matchTest = new Match(players);
        matchTest.setId(100);
        matchTest.setMatchStatus(MatchStatus.PLAYING);
        matchTest.setBoard(boardTest);


        List<Turn> listTurn = new ArrayList<>();
        //Generamos los turnos
        consulTest = new Consul(playerConsul);
        pretorTest = new Pretor(playerPretor);

        FactionCard factionCard1_test = new FactionCard(playerPlayer, matchTest, Faction.LOYAL);
        FactionCard factionCard2_test = new FactionCard(playerPlayer, matchTest, Faction.LOYAL);



        List<FactionCard> factionCardList = List.of(factionCard1_test, factionCard2_test);
        turnTest = new Turn(matchTest, 0, consulTest);
        edilTest1 = new Edil(turnTest, playerEdil1);
        edilTest2 = new Edil(turnTest, playerEdil2);
        edilTest1.setVote(VOTE.LOYAL);
        edilTest2.setVote(VOTE.TRAITOR);
        List<Edil> edilList = List.of(edilTest1, edilTest2);
        turnTest.setPlayerEdil(edilList);
        turnTest.setPretor(pretorTest);

        turnTest.setMatchTurnStatus(MatchTurnStatus.WAITTOVOTE);
        turnTest.setNextStateTurn(LocalTime.now().plusSeconds(10));
        listTurn.add(turnTest);


        given(this.factionService.findByMatchAndPlayer(matchTest, playerAnfitrion)).willReturn(factionCardList);
        given(this.matchService.findMatchById(100)).willReturn(Optional.of(matchTest));
        given(this.turnService.findTurnByMatch(matchTest)).willReturn(listTurn);


        turnTest.setNextStateTurn(LocalTime.now().minusSeconds(10));


        mockMvc.perform(get("/match/100"))
            .andExpect(status().isOk())
            .andExpect(view().name("matchs/match"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("playerCredentials"))
            .andExpect(model().attributeExists("match"))
            .andExpect(model().attributeDoesNotExist("factionSelected"))
            .andExpect(model().attributeExists("turn"))
            .andExpect(model().attributeExists("round"));
    }



    //Fail
    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void testCreateMatchPlayingCHOOSE_ROL() throws Exception{
        Match m = generateMatch();
        matchTest.setMatchStatus(MatchStatus.PLAYING);
        Turn turn = generateTurn();
        turnTest.getConsul().setFaction(null);
        turnTest.setMatchTurnStatus(MatchTurnStatus.CHOOSE_ROL);
        turnTest.setTurn(7);
        turnTest.setNextStateTurn(LocalTime.now().plusSeconds(10));
        turnTest.setPretor(null);
        turnTest.setMatch(matchTest);
        matchTest.setTurnList(List.of(turnTest));
        generateTurns(7);


        mockMvc.perform(get("/match/100"))
            .andExpect(status().isOk())
            .andExpect(view().name("matchs/match"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("playerCredentials"))
            .andExpect(model().attributeExists("match"))
            .andExpect(model().attributeDoesNotExist("factionSelected"))
            .andExpect(model().attributeExists("turn"))
            .andExpect(model().attributeExists("round"))
            .andExpect(model().attributeExists("rolTypeValor"));

    }



    // EdilCambiaUnVoto
    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void testCreateMatchPlayingRevote() throws Exception{
        Match m = generateMatch();
        matchTest.setMatchStatus(MatchStatus.PLAYING);
        Turn turn = generateTurn();
        turnTest.setNextStateTurn(LocalTime.now().plusSeconds(20));
        turnTest.getConsul().setFaction(null);
        turnTest.setMatchTurnStatus(MatchTurnStatus.REVOTE);
        turnTest.getPretor().setEdil(turnTest.getPlayerEdil().get(0));
        matchTest.setTurnList(List.of(turnTest));


        mockMvc.perform(get("/match/100"))
            .andExpect(status().isOk())
            .andExpect(view().name("matchs/match"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("playerCredentials"))
            .andExpect(model().attributeExists("match"))
            .andExpect(model().attributeDoesNotExist("factionSelected"))
            .andExpect(model().attributeExists("turn"))
            .andExpect(model().attributeExists("round"))
            .andExpect(model().attributeExists("edilChanged"));
    }

    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void testCreateMatchPlayingNewTurn() throws Exception{
        Match m = generateMatch();
        matchTest.setMatchStatus(MatchStatus.PLAYING);
        Turn turn = generateTurn();
        turnTest.setNextStateTurn(LocalTime.now().minusSeconds(20));
        turnTest.getConsul().setFaction(null);
        turnTest.setMatchTurnStatus(MatchTurnStatus.CONT);
        matchTest.setTurnList(List.of(turnTest));


        mockMvc.perform(get("/match/100"))
            .andExpect(status().isOk())
            .andExpect(view().name("matchs/match"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("playerCredentials"))
            .andExpect(model().attributeExists("match"))
            .andExpect(model().attributeDoesNotExist("factionSelected"))
            .andExpect(model().attributeExists("turn"))
            .andExpect(model().attributeExists("round"));
    }
   

    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void testListForum() throws Exception{
        List<Match> matches = new ArrayList<>();
        given(matchService.findAllDesc()).willReturn(matches);


        mockMvc.perform(get("/forum"))
            .andExpect(status().isOk())
            .andExpect(view().name("/forum/forum"))
            .andExpect(model().attributeExists("matchs"));

    }


    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void testListForums() throws Exception{
        Integer forum_id = 10;

        List<Comment> comments = new ArrayList<>();
        given(commentService.findCommentsByMatchId(forum_id)).willReturn(comments);


        mockMvc.perform(get("/forum/"+ forum_id))
            .andExpect(status().isOk())
            .andExpect(view().name("/forum/forumComments"))
            .andExpect(model().attributeExists("comments"))
            .andExpect(model().attributeExists("comment"));;

    }

    @WithMockUser(username = USERNAME_TEST, authorities = {"PLAYER"})
    @Test
    void precessCreatingForm() throws Exception{
        Match m = generateMatch();
        Integer forum_id = 100;

        mockMvc.perform(post("/forum/"+forum_id)
                .param("comment", "PlayerTest1@email.com").with(csrf()))
            .andExpect(view().name("redirect:/forum/100"));


    }

    @WithMockUser(username = USERNAME_TEST, authorities = {"admin"})
    @Test
    void listMatchs() throws Exception{
        List<Match> matches = new ArrayList<>();
        given(matchService.findAll()).willReturn(matches);


        mockMvc.perform(get("/admin/matchsList"))
            .andExpect(status().isOk())
            .andExpect(view().name("matchs/matchsList"))
            .andExpect(model().attributeExists("matchs"));


    }

    @WithMockUser(username = USERNAME_TEST, authorities = {"admin"})
    @Test
    void listMatchInfo() throws Exception{
        Match m = generateMatch();


        List<Match> matches = new ArrayList<>();
        given(matchService.findMatchById(m.getId())).willReturn(Optional.of(matchTest));
        given(playerService.findAll()).willReturn(matchTest.getPlayerList());
        given(playerService.findPlayerByMatchId(m.getId())).willReturn(matchTest.getPlayerList());




        mockMvc.perform(get("/admin/matchsList/matchinfo/"+m.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("matchs/matchInfo"))
            .andExpect(model().attributeExists("match"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("playersPlaying"));


    }
}

