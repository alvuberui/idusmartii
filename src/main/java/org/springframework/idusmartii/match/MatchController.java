package org.springframework.idusmartii.match;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.consul.Consul;
import org.springframework.idusmartii.consul.ConsulService;
import org.springframework.idusmartii.edil.Edil;
import org.springframework.idusmartii.edil.EdilService;
import org.springframework.idusmartii.edil.VOTE;
import org.springframework.idusmartii.estadistics.Estadistics;
import org.springframework.idusmartii.estadistics.EstadisticsService;
import org.springframework.idusmartii.faction.Faction;
import org.springframework.idusmartii.faction.FactionCard;
import org.springframework.idusmartii.faction.FactionService;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.board.Board;
import org.springframework.idusmartii.board.BoardService;
import org.springframework.idusmartii.comment.Comment;
import org.springframework.idusmartii.comment.CommentService;
import org.springframework.idusmartii.pretor.Pretor;
import org.springframework.idusmartii.turn.Turn;
import org.springframework.idusmartii.turn.TurnService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MatchController {

    // El tiempo TIEMPO_ENTRE_TURNOS >= REFRESCO_PAGINA*2
    private final Integer TIEMPO_ENTRE_TURNOS = 7;
    private final Integer REFRESCO_PAGINA = 3;

    @Autowired
    private MatchService matchService;

    @Autowired
    private PlayerService playerService;


    @Autowired
    private BoardService boardService;


    @Autowired
    private FactionService cardService;

    @Autowired
    private CommentService commentService;


    @Autowired
    private EdilService edilService;

    @Autowired
    private TurnService turnService;


    @Autowired
    private ConsulService consulService;

    @Autowired
    private FactionService factionService;

    @Autowired
    private EstadisticsService estadisticsService;

    /*
     * alvuberui: Crear la partida con los jugadores a partir de la mesa
     * y se genera la baraja de facción (2 cartas de facción a cada jugador al azar)
     */
    @GetMapping("/match/new/{boardId}")
    public String createMatch(@PathVariable("boardId") int boardId){
        /* Se crea la partida a partir de la mesa */
        Board b = this.boardService.findById(boardId).get();
        Collection<Player> players = playerService.findPlayerByBoardId(b.getId());
        List<Player> playersList = new ArrayList<Player>(players);
        Match m = new Match(playersList.size());
        m.setBoard(b);
        b.setPlayingMatch(m);// Consta en la mesa de que se esta jugando esta partida
        matchService.save(m);
        boardService.saveBoard(b);

        /* Se genera la baraja de facción para cada jugador (2 cartas a cada uno al azar) */
        //Generacion de la baraja ## Para el garro: probar la funciond de Util cuando este lista
        List<Faction> t =  m.generarBarajaRandom();
        for(int i=0; i < playersList.size(); i++) {
            Player p = playersList.get(i);
            //Asignacion de cartas a cada persona
            cardService.save(new FactionCard(p, m, t.get(i*2)));
            cardService.save(new FactionCard(p, m, t.get((i*2)+1)));
            p.setMatch(m);
            playerService.savePlayer(p);
        }
        System.out.println("Match Created");
        return "redirect:/match/" + m.getId();
    }
    /*
     * alvuberui: controlador con toda la ejecución de la partida.
     */
    @GetMapping("/match/{matchId}")
    public ModelAndView showMatch(@PathVariable("matchId") int matchId, HttpServletResponse response) throws Exception {
        response.addHeader("Refresh" , REFRESCO_PAGINA.toString());
        ModelAndView mav = new ModelAndView("matchs/match");

        /* Obtiene la autentificación */
        Authentication a = SecurityContextHolder.getContext().getAuthentication();

        Player p = playerService.getByUsername(a.getName());



        /* Obtiene la partida a partir de us id */
        Match m = this.matchService.findMatchById(matchId).get();
        List<Player> players = m.getPlayerList();
        mav.addObject("players", m.getPlayerList());

        boolean isAnfitrion = p.getId() == m.getBoard().getAnfitrion().getId();


        Collection<Turn> turnList = turnService.findTurnByMatch(m);
        Optional<Turn> optionalTurn = turnList.stream().filter(x->x.getTurn() == turnList.size()-1).findFirst();
        // Cosas de players

        mav.addObject("playerCredentials",p);

        Optional<Consul> factionSelected = consulService.getConsulByPlayer(p, m);
        if(factionSelected.isPresent() && factionSelected.get().getFaction() != null){
            mav.addObject("factionSelected",factionSelected.get().getFaction());
        }


        //Posible conflicto con el match

        //Ejecucion del match (Solo lo ejecuta el anfitrion del match)
        if(m.getMatchStatus() == MatchStatus.PLAYING && optionalTurn.isPresent()){

            // Ejecucion de turnos
            Turn turn = optionalTurn.get();


            mav.addObject("turn",turn);mav.addObject("round", turn.getRound());
            if(turn.getMatchTurnStatus() == MatchTurnStatus.REVOTE){
                if(turn.getPretor() != null && turn.getPretor().getEdil() != null){
                    mav.addObject("edilChanged",turn.getPretor().getEdil());
                }
            }

            
         if(isAnfitrion && !turn.getNextStateTurn().isBefore(LocalTime.now()) && turn.getMatchTurnStatus() == MatchTurnStatus.CONT && turn.getPlayerEdil().size() != 0){
                // setEdiles
                List<Edil> ediles = turn.getPlayerEdil();
                for(Edil edil : ediles){
                    if(edil.getVote() != null){
                        updateSufragium(edil, m, turn);
                    }
                }
                turn.setNextStateTurn(LocalTime.now().minusSeconds(10));
                turnService.save(turn);
         }
         
            if(isAnfitrion && turn.getNextStateTurn().isBefore(LocalTime.now())){
                actionMatch(m, turn);
            }
            // debe ir dentro de las funciones del anfitrion



            //Enviar informacion necesario a jugadores que no son administradores
            if(turn.getRound() == 2 &&  turn.getMatchTurnStatus() == MatchTurnStatus.CHOOSE_ROL){
                if(turn.getPretor() == null) {
                    mav.addObject("rolTypeValor", 1);
                }else if(turn.getPlayerEdil().size() < 2){
                    mav.addObject("rolTypeValor",2);
                }else{
                    mav.addObject("rolTypeValor",3);
                }
            }else{
                if(turn.getPretor() == null){
                    List<Player> pretorList = m.getNextPretorList(getPlayerWithNotRoll(players,optionalTurn));
                    Collections.shuffle(pretorList);
                    turn.setPretor(new Pretor(pretorList.get(0)));
                }
                if(turn.getPlayerEdil().size() != 2){
                    List<Player> edilList = m.getNextPretorList(getPlayerWithNotRoll(players,optionalTurn));
                    Collections.shuffle(edilList);
                    for(Player player : edilList){
                        if(turn.getPlayerEdil().size() != 2){
                            Edil edil = new Edil(turn, player);
                            edilService.save(edil);
                            turn.getPlayerEdil().add(edil);
                        }
                    }

                }
            }
            mav.addObject("players", players);
            mav.addObject("factionCard1",auxGetFactionCards(players,m).get(p).get(0));
            mav.addObject("factionCard2",auxGetFactionCards(players,m).get(p).get(1));
            mav.addObject("playerWithNotRoll",getPlayerWithNotRoll(players,optionalTurn));
        }

        if(m.getMatchStatus() == MatchStatus.WAITTING &&
            isAnfitrion  &&
            m.getStartMatch().isBefore(LocalDateTime.now())) {
            /* Se genera el primer turno */
            Turn newTurn = new Turn(m, 0);

            turnService.save(m.makeTurn(newTurn, TIEMPO_ENTRE_TURNOS));
            m.setMatchStatus(MatchStatus.PLAYING);
            matchService.save(m);
        }
        //Modelos

        mav.addObject("match",m);



        return mav;
    }


    private void turns2(Turn turn, List<Player> players, Match m) {
       if(turn.getPretor() != null && turn.getRound() == 2 &&
            turn.getPretor().getEdil() != null &&
            (turn.getPretor().getEdil().getVote() == VOTE.NEUTRAL || turn.getPretor().getEdil().getVote() == null ) && turn.getMatchTurnStatus() == MatchTurnStatus.REVOTE){
            turn.nextStatusTurn(TIEMPO_ENTRE_TURNOS);
            turn.setMatchTurnStatus(MatchTurnStatus.CONT);
            turnService.save(turn);
        }else{
            turn.nextStatusTurn(TIEMPO_ENTRE_TURNOS);
            turnService.save(turn);
        }
    }

    private void updateSufragium(Edil edil, Match match, Turn turn){
        if(edil.getVote() == VOTE.LOYAL){
            match.setVotosLeal(match.getVotosLeal()+1);
        }else if(edil.getVote() == VOTE.TRAITOR){
            match.setVotosTraidor(match.getVotosTraidor()+1);
        }
        Integer votosLeal = match.getVotosLeal();
        Integer votosTraidor = match.getVotosTraidor();


        if(match.getNumPlayers() == 5 && (votosLeal > 13 || votosTraidor > 13)){
            setWinnerAux(13, match, turn);
        }else if(match.getNumPlayers() == 6 && (votosLeal > 15 || votosTraidor > 15)){
            setWinnerAux(15, match, turn);
        }else if(match.getNumPlayers() == 7 && (votosLeal > 17 || votosTraidor > 17)){
            setWinnerAux(17, match, turn);
        }else if(match.getNumPlayers() == 8 && (votosLeal > 20 || votosTraidor > 20)){
            setWinnerAux(20, match, turn);

        }
        matchService.save(match);

    }
    private void setWinnerAux(Integer expectedVotes, Match match, Turn turn){
        if(match.getVotosLeal() > expectedVotes){
            match.setWinner(Faction.LOYAL);
        }else{
            match.setWinner(Faction.TRAITOR);
        }
        finishMatch(match, turn);
    }
    private void actionMatch(Match m, Turn turn) throws Exception {
        if(turn.getMatchTurnStatus() == MatchTurnStatus.CHOOSE_FACCTION || (turn.getTurn() == 0 && turn.getMatchTurnStatus() == MatchTurnStatus.CONT)|| (turn.getTurn() > m.getNumPlayers() && turn.getMatchTurnStatus() == MatchTurnStatus.CONT)){
            // Final del turn
            if(turn.getTurn()+1 >= (m.getNumPlayers())*2){
                if(m.getVotosLeal()-m.getVotosTraidor() >= 2){
                    m.setWinner(Faction.LOYAL);
                }else if(m.getVotosTraidor()-m.getVotosLeal() >= 2){
                    m.setWinner(Faction.TRAITOR);
                }else{
                    m.setWinner(Faction.MERCHANT);
                }

                finishMatch(m, turn);

                // Aparece un boton en pantalla para volver a la board
            }else  if(turn.getTurn()+1 >= m.getNumPlayers()){
                // Segunda ronda
                // Solo exite un consul por persona en cada match pero un pretor o edil por turno para cada persona
                Consul consul = m.getTurnList().stream()
                    .filter(x-> x.getTurn()==(turn.getTurn()+1)%(m.getNumPlayers()))
                    .map(x->x.getConsul()).collect(Collectors.toList()).get(0);
                Turn newTurn = m.makeTurn(new Turn(m, turn.getTurn()+1, consul), TIEMPO_ENTRE_TURNOS*2);
                turnService.save(newTurn);
            }else{
                //Primera ronda
                Turn newTurn = m.makeTurn(new Turn(m, turn.getTurn() + 1), TIEMPO_ENTRE_TURNOS+5);
                turnService.save(newTurn);
            }
        }else{
            turns2(turn,  m.getPlayerList(), m);
        }
    }
    private void finishMatch(Match m, Turn turn){
        System.out.println("Terminando Match");
        // Final del juego
        // Echar a los jugadores del match

        turn.setNextStateTurn(LocalTime.now());
        turnService.save(turn);
        m.setMatchStatus(MatchStatus.FINISHED);
        matchService.save(m);
        System.out.println("match save");
        for(Player player : m.getPlayerList() ){
            System.out.println("for player");
            player.setMatch(null);
            playerService.savePlayer(player);
            increaseEstadistics(player, m);
            calculateRankingPos(player);

        }
        m.getBoard().setPlayingMatch(null);
        boardService.saveBoard(m.getBoard());


    }
    private List<Player> getPlayerWithNotRoll(List<Player> players, Optional<Turn> OptionalTurn){
        if(OptionalTurn.isPresent()){
            Turn turn = OptionalTurn.get();
            List<Player> playerWithoutRoll = new ArrayList<>(players);
            if(turn.getConsul() != null){
                playerWithoutRoll.remove(turn.getConsul().getPlayer());
            }
            if(turn.getPretor() != null){
                playerWithoutRoll.remove(turn.getPretor().getPlayer());
            }
            if(turn.getPlayerEdil() != null){
                List<Player> ediles = edilService.getByTurn(turn).stream().map(x->x.getPlayer()).collect(Collectors.toList());
                for(Player e : ediles){
                    playerWithoutRoll.remove(e);
                }
            }
            return playerWithoutRoll;
        }else{
            return players;
        }
    }


    private Map<Player,List<FactionCard>> auxGetFactionCards(List<Player> players, Match m) {
        Map<Player,List<FactionCard>> res = new HashMap<Player, List<FactionCard>>();
        for(int i=0; i<players.size(); i++) {
            Player p = players.get(i);
            List<FactionCard> cards = (List<FactionCard>) factionService.findByMatchAndPlayer(m, p);
            res.put(p, cards);
        }
        return res;
    }
    /*
     * alvuberui: vista de partidas para administradores
     */
    @GetMapping("/admin/matchsList")
    public String listMatchs(ModelMap modelMap) {
        String vista = "matchs/matchsList";
        Iterable<Match>  matchs = matchService.findAll();
        modelMap.addAttribute("matchs", matchs);
        return vista;
    }
    /*
     * alvuberui: controlador para la vista de información
     * detallada de la mesa.
     */
    @GetMapping("/admin/matchsList/matchinfo/{matchId}")
    public String listMatchs(ModelMap modelMap, @PathVariable("matchId") int matchId) {
        String vista = "matchs/matchInfo";
        Match  match = matchService.findMatchById(matchId).get();
        Iterable<Player> players = playerService.findAll();
        Iterable<Player> playersPlaying = playerService.findPlayerByMatchId(matchId);
        modelMap.addAttribute("match", match);
        modelMap.addAttribute("players", players);
        modelMap.addAttribute("playersPlaying", playersPlaying);
        return vista;
    }


    /*
     *
     * Metodo para incrementar estadisticas de cada jugador juagomram4
     *
     */

	    private void increaseEstadistics(Player player,Match playedMatch) {

	    	LocalTime tiempoFinal = null;

	    	if(playedMatch.getTurnList().get(playedMatch.getTurnList().size()-1).getNextStateTurn() == null) {
	    		tiempoFinal = LocalTime.now();
	    	}else {
	    		tiempoFinal = playedMatch.getTurnList().get(playedMatch.getTurnList().size()-1).getNextStateTurn();
	    	}
	    	Duration matchTyme = Duration.between(playedMatch.getStartMatch().toLocalTime(), tiempoFinal);
			Estadistics estadistic = player.getEstadistic();
			playerMatchsDurationChecker(matchTyme,estadistic);
			playerWLMatchChecker(playedMatch,player);
			estadistic.setMatchsPlayed(estadistic.getMatchsPlayed()+1);
			estadisticsService.save(estadistic);

		}

		private void playerWLMatchChecker(Match playedMatch, Player player) {
			Estadistics estadistic = player.getEstadistic();
			FactionCard playerFaction = consulService.getConsulByPlayer(player, playedMatch).get().getFaction();
			if(playerFaction!= null) {
				if(playedMatch.getWinner() == playerFaction.getCardType()) {
					estadistic.setMatchsWins(estadistic.getMatchsWins()+1);
					estadistic.setPoints(estadistic.getPoints()+3);
					estadistic.setActualWinStrike(estadistic.getActualWinStrike()+1);
					if(estadistic.getActualWinStrike()>estadistic.getWinsLongerStrike()) {
						estadistic.setWinsLongerStrike(estadistic.getActualWinStrike());
					}
				} else {
					estadistic.setMatchsLoses(estadistic.getMatchsLoses()+1);
					if(estadistic.getPoints()>= 2) {
						estadistic.setPoints(estadistic.getPoints()-2);
					} else {
						estadistic.setPoints(0);
					}

					estadistic.setActualWinStrike(0);

				}
			}



		}

		private void playerMatchsDurationChecker(Duration matchTyme, Estadistics estadistic) {
			if(estadistic.getMatchLongerDuration() == null && estadistic.getMatchShorterDuration() == null) {
				estadistic.setMatchLongerDuration((int) matchTyme.getSeconds());
				estadistic.setMatchShorterDuration((int) matchTyme.getSeconds());
			}
			Integer secondsLongestMatch = estadistic.getMatchLongerDuration();
			Integer secondsShorterMatch = estadistic.getMatchShorterDuration();
			if ((int)matchTyme.getSeconds()>secondsLongestMatch) {
				estadistic.setMatchLongerDuration((int) matchTyme.getSeconds());
			} else if ((int)matchTyme.getSeconds()<secondsShorterMatch) {
				estadistic.setMatchShorterDuration((int) matchTyme.getSeconds());
			}

		}


		private void calculateRankingPos(Player player) {
            Estadistics estadistic = player.getEstadistic();
            List<Player> playerList = StreamSupport.stream(playerService.findAll().spliterator(), false).collect(Collectors.toList());

            playerList =  playerList.stream().filter(x-> x.getEstadistic() != null).collect(Collectors.toList());

            playerList =  playerList.stream().filter(x-> x.getEstadistic().getPoints() != null).collect(Collectors.toList());


            System.out.println(playerList);

            Comparator<Player> comparatorPoints = Comparator.comparing((Player p) -> p.getEstadistic().getPoints());

            playerList.sort(comparatorPoints.reversed());

            Integer pos = playerList.indexOf(player);
            estadistic.setRankingPos(pos+1);

            estadisticsService.save(estadistic);
        }

    /*
     * alvuberui: controlador para la vista de foros de cada partida
     */
    @GetMapping("/forum")
    public String listForums(ModelMap modelMap) {
        String vista = "/forum/forum";
        List<Match>  matchs = (List<Match>) matchService.findAllDesc();
        modelMap.addAttribute("matchs", matchs);
        return vista;
    }

    @GetMapping("/forum/{forumId}")
    public String listForums(ModelMap modelMap, @PathVariable("forumId") int forumId) {
        String vista = "/forum/forumComments";
        Comment comment = new Comment();
        List<Comment>  comments = (List<Comment>) commentService.findCommentsByMatchId(forumId);
        modelMap.addAttribute("comments", comments);
        modelMap.addAttribute("comment", comment);
        return vista;
    }

    @PostMapping(value = "/forum/{forumId}")
    public String proccessCreatingForm(@Valid String comment, @PathVariable("forumId") int matchId) {
        Comment c = new Comment();
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        Player p = playerService.getByUsername(a.getName());
        c.setDate(LocalDateTime.now());
        c.setComment(comment);
        c.setMatch(matchService.findMatchById(matchId).get());
        c.setPlayer(p);
        commentService.save(c);
		return "redirect:/forum/"+matchId;
	}

}
