package org.springframework.idusmartii.pretor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.board.BoardService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
public class PretorController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private PlayerService playerService;


    @Autowired
    private TurnService turnService;

    @Autowired
    private BoardService boardService;


    @Autowired
    private FactionService factionService;


    @Autowired
    private EdilService edilService;

    @Autowired
    private PretorService pretorService;


    @GetMapping("/match/{matchId}/action/pretor/changeVote/{edilId}")
    public String edilRevote(@PathVariable("matchId") int matchId,
                             @PathVariable("edilId") Integer edilId) {
        Match match = this.matchService.findMatchById(matchId).get();
        Turn thisTurn = match.getTurnList().stream().sorted(Comparator.comparing(Turn::getTurn)).collect(Collectors.toList()).get(match.getTurnList().size()-1);
        Authentication a = SecurityContextHolder.getContext().getAuthentication();


        if(a!=null && a.getPrincipal().toString() != "anonymousUser"){
            Player player = playerService.getByUsername(a.getName());
            Pretor pretor = thisTurn.getPretor();
            Edil edil = edilService.findById(edilId).get();
            Integer i = thisTurn.getPlayerEdil().indexOf(edil);
            if(pretor.getPlayer() == player && thisTurn.getMatchTurnStatus() == MatchTurnStatus.CHANGING_VOTE && i != -1){
                // pretor cambia las cartas de uno de los dos ediles
                //Ronda 2, Se dice que el pretor tiene un edil X para que revote (el revote esta en edil)
                if(thisTurn.getRound() == 2){
                    thisTurn.setNextStateTurn(LocalTime.now().plusSeconds(6));
                    pretor.setEdil(edil);
                    pretorService.save(pretor);
                    turnService.save(thisTurn);
                }else{
                    // Ronda 1, automaticamente se cambia la carta
                    thisTurn.setNextStateTurn(LocalTime.now().plusSeconds(6)); // suma 6 segunodos a la ronda
                    if(pretor.getEdil() != null && pretor.getEdil().equals(edil)){
                        thisTurn.setMatchTurnStatus(MatchTurnStatus.CONT);
                        if(edil.getVote() == VOTE.LOYAL){
                            edil.setVote(VOTE.TRAITOR);
                            edil.setVoteChange(VOTE.LOYAL);
                        }else{
                            edil.setVote(VOTE.LOYAL);
                            edil.setVoteChange(VOTE.TRAITOR);
                        }
                        edilService.save(edil);
                    }else{
                        pretor.setEdil(edil);
                        pretorService.save(pretor);
                    }
                    turnService.save(thisTurn);
                }
            }
        }
        return "redirect:/match/"+matchId;
    }
}
