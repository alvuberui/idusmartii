package org.springframework.idusmartii.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.faction.Faction;
import org.springframework.idusmartii.faction.FactionCard;
import org.springframework.idusmartii.faction.FactionService;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.players.Rol;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.transaction.annotation.Transactional;

public class MatchUtils {
    @Autowired
    private static PlayerService playerService;
    private static FactionService cardService;
    private static UserService userService;
    private static AuthoritiesService authoritiesService;






    //Cambio de Rols

    public static HashMap<Player, Rol> setRol(Integer ConsulPlayerIndex, List<Player> playerList){
        HashMap<Player, Rol> mapOfRols = new HashMap<>();
        for(int i = 0;i<playerList.size(); i++){
            Player player = playerList.get(i);
            Rol role = null;
            if( i==(ConsulPlayerIndex)%playerList.size() || i==(ConsulPlayerIndex+1)%playerList.size()) {
                role = Rol.EDIL;
            }else if(i==(ConsulPlayerIndex+2)%playerList.size()) {
                role = Rol.PRETOR;
            }
            else if(i==(ConsulPlayerIndex+3)%playerList.size()) {
                role = Rol.CONSUL;
            }
            mapOfRols.put(player, role);
        }
        return mapOfRols;
    }

    // Generacion de Baraja

    public static void generarBaraja(Match match, List<Player> playerList){
        List<Faction> t =  match.generarBarajaRandom();
        for(int i=0; i < playerList.size(); i++) {
            Player p = playerList.get(i);
            //Asignacion de cartas a cada persona
            cardService.save(new FactionCard(p, match, t.get(i*2)));
            cardService.save(new FactionCard(p, match, t.get((i*2)+1)));
            p.setMatch(match);
            playerService.savePlayer(p);
        }
    }


}
