package org.springframework.idusmartii.edil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.board.BoardService;
import org.springframework.idusmartii.faction.FactionService;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchService;
import org.springframework.idusmartii.match.MatchTurnStatus;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.turn.Turn;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class EdilController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private FactionService factionService;

    @Autowired
    private EdilService edilService;

    @GetMapping("/match/{matchId}/action/edil/vote/{vote}")
    public String edilVote(@PathVariable("matchId") int matchId, @PathVariable("vote") Integer vote) {
        Match match = this.matchService.findMatchById(matchId).get();
        Integer players = match.getNumPlayers();
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        Turn thisTurn = match.getTurnList().stream().sorted(Comparator.comparing(Turn::getTurn)).collect(Collectors.toList()).get(match.getTurnList().size()-1);
        if(a!=null && a.getPrincipal().toString() != "anonymousUser"){
            Player player = playerService.getByUsername(a.getName());
            Optional<Edil> edil =  thisTurn.getPlayerEdil().stream().filter(x-> x.getPlayer() == player).findFirst();
            if(edil.isPresent()){
                if(thisTurn.getMatchTurnStatus() == MatchTurnStatus.WAITTOVOTE || thisTurn.getMatchTurnStatus() == MatchTurnStatus.REVOTE){
                    //Acciones del edil
                    // 1 = LOYAL, 2 = TRAITOR , 3 = NEUTRAL

                    if(vote == 3 && thisTurn.getRound() == 2){
                        edil.get().setVote(VOTE.NEUTRAL);
                    }else if(vote == 1){
                        if(thisTurn.getRound() == 2 && edil.get().getVote() == VOTE.NEUTRAL &&  thisTurn.getMatchTurnStatus() == MatchTurnStatus.REVOTE){
                            edil.get().setVote(VOTE.LOYAL);
                            edil.get().setVoteChange(VOTE.NEUTRAL);
                        }else if(thisTurn.getMatchTurnStatus() == MatchTurnStatus.WAITTOVOTE){
                            edil.get().setVote(VOTE.LOYAL);
                        }
                    }else if(vote == 2){
                        if(thisTurn.getRound() == 2 && edil.get().getVote() == VOTE.NEUTRAL &&  thisTurn.getMatchTurnStatus() == MatchTurnStatus.REVOTE){
                            edil.get().setVote(VOTE.TRAITOR);
                            edil.get().setVoteChange(VOTE.NEUTRAL);
                        }else if( thisTurn.getMatchTurnStatus() == MatchTurnStatus.WAITTOVOTE){
                            edil.get().setVote(VOTE.TRAITOR);
                        }
                    }
                    edilService.save(edil.get());
                }
            }
        }
        return "redirect:/match/"+matchId;
    }
}
