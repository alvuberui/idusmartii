package org.springframework.idusmartii.consul;


import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.idusmartii.edil.Edil;
import org.springframework.idusmartii.edil.EdilService;
import org.springframework.idusmartii.faction.Faction;
import org.springframework.idusmartii.faction.FactionCard;
import org.springframework.idusmartii.faction.FactionService;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchService;
import org.springframework.idusmartii.match.MatchTurnStatus;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.pretor.Pretor;
import org.springframework.idusmartii.turn.Turn;
import org.springframework.idusmartii.turn.TurnService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;


@Controller
public class ConsulController {

    @Autowired
	private MatchService matchService;

	@Autowired
	private PlayerService playerService;

    @Autowired
    private FactionService factionService;

    @Autowired
    private TurnService turnService;

    @Autowired
    private ConsulService consulService;

    @Autowired
    private EdilService edilService;

    @GetMapping("/match/{matchId}/action/consul/chooseFaction/{factionId}")
    public String edilVote(@PathVariable("matchId") int matchId,
                           @PathVariable("factionId") Integer factionId, HttpServletResponse response) {
        Match match = this.matchService.findMatchById(matchId).get();
        Turn lastTurn = match.getTurnList().stream().sorted(Comparator.comparing(Turn::getTurn)).collect(Collectors.toList()).get(match.getTurnList().size()-1);
        Integer players = match.getNumPlayers();
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if(a!=null && a.getPrincipal().toString() != "anonymousUser"){
            Player player = playerService.getByUsername(a.getName());
            Consul consul = lastTurn.getConsul();
            if(consul != null && consul.getPlayer() == player && lastTurn.getMatchTurnStatus() == MatchTurnStatus.CHOOSE_FACCTION){
                // Elige una de las dos opciones que tiene el consul
                List<FactionCard> cards = new ArrayList<>(factionService.findByMatchAndPlayer(match, player));
                if(factionId == 1){ // LOYAL
                    Optional<FactionCard> loyalCard = cards.stream().filter(x-> x.getCardType() == Faction.LOYAL).findFirst();
                    loyalCard.ifPresent(consul::setFaction);
                    if (loyalCard.isEmpty()){
                        System.out.println("No hay leal");
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }else if(factionId == 2){ // TRAITOR
                    Optional<FactionCard> traitorCard = cards.stream().filter(x-> x.getCardType() == Faction.TRAITOR).findFirst();
                    traitorCard.ifPresent(consul::setFaction);
                }else if(factionId == 3){ //MERCHANT
                    Optional<FactionCard> merchantCard = cards.stream().filter(x-> x.getCardType() == Faction.MERCHANT).findFirst();
                    merchantCard.ifPresent(consul::setFaction);
                }
                consulService.save(consul);
            }
        }
        return "redirect:/match/"+matchId;
    }

    @GetMapping("/match/{matchId}/action/consul/chooseRol/{playerId}/{rolType}")
    public String consulChooseRol(@PathVariable("matchId") int matchId,
                                  @PathVariable("playerId") int playerId,
                                  @PathVariable("rolType") int rolType) {

        Match match = this.matchService.findMatchById(matchId).get();
        Integer turns = match.getTurnList().size();
        List<Turn> turnList = match.getTurnList().stream().filter(x-> x.getTurn()==match.getTurnList().size()).collect(Collectors.toList());
        Turn thisturn = match.getTurnList().stream().sorted(Comparator.comparing(Turn::getTurn)).collect(Collectors.toList()).get(turns-1);
        Turn lastturn = match.getTurnList().stream().sorted(Comparator.comparing(Turn::getTurn)).collect(Collectors.toList()).get(turns-2);
        List<Player> players = match.getPlayerList();
        Player playerToChange  = playerService.findPlayerById(playerId).get();
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if(a!=null && a.getPrincipal().toString() != "anonymousUser"){
            Player player = playerService.getByUsername(a.getName());
            Consul consul = thisturn.getConsul();
            if(players.indexOf(playerToChange) != -1){ //Revisar esto por que no tiene sentido y hay que quitarlo
                if(consul != null && consul.getPlayer() == player && thisturn.getMatchTurnStatus() == MatchTurnStatus.CHOOSE_ROL){
                    if(rolType == 1){
                        if(thisturn.getPretor() == null && lastturn.getPretor().getPlayer() != playerToChange){
                            thisturn.setPretor(new Pretor(playerToChange));
                            turnService.save(thisturn);
                        }
                    }else if(rolType == 2){ //En duda de si hacer esto o hacer un while hasta que el tamaño de Ediles sea 2
                            if(thisturn.getPlayerEdil().size() < 2 && ((match.getNumPlayers() <= 5 && thisturn.getPretor() != null)
                                || (match.getNumPlayers() > 5 && !lastturn.getPlayerEdil().stream().map(Edil::getPlayer).collect(Collectors.toList()).contains(playerToChange)))){
                            		Edil newEdil = new Edil(thisturn,playerToChange);
                                    edilService.save(newEdil);
                            }
                    }
                }
            }
        }
        return "redirect:/match/"+matchId;
    }

}
