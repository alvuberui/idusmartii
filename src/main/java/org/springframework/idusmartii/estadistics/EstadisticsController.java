package org.springframework.idusmartii.estadistics;



import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.faction.Faction;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchService;
import org.springframework.idusmartii.players.IteratorToStream;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EstadisticsController {
	
	
	@Autowired
    private EstadisticsService estadisticsService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private MatchService matchService;
	
	@GetMapping("/globalEstadistics")
	public String globalEstadistics(ModelMap modelMap) {
		String vista = "estadistics/globalEstadistics";
		
		
		//---------- TOP TEN RANKING------------//
		
		List<Player> topTen = getTopTenRanking();

		
		//........... GLOBAL ESTADISTICS -------//
		Estadistics globalEstadistic = new Estadistics();
		List<Player> playerList = allPlayersInList();
		Integer totalPlayers = playerList.size();
		getGlobalEstadistics(globalEstadistic,playerList);
		

		
		//--------------------OWN POSITION ON RANKING----------------------//
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player actualPlayer = playerService.getByUsername(authentication.getName());
        
		//--------------------SHORTER AN LONGEST MATCH IN STRINGS -------------------//
        
        longuestDurationGE(playerList, globalEstadistic);
        shorterDurationGE(playerList, globalEstadistic);
        String longuest = IntegerAMinSegs(globalEstadistic.getMatchLongerDuration());
        String shortest = IntegerAMinSegs(globalEstadistic.getMatchShorterDuration());
        
        //--------------------FACTION WITH MORE WINS -------------------//
        
        String factionMostWinned = FaccionConMasPartidasGanadas();
        		
		//---------------------VIEW ATTRIBUTES------------------------//
        
		modelMap.addAttribute("globalEstadistic",globalEstadistic);
		modelMap.addAttribute("playerRanking",topTen);
		modelMap.addAttribute("totalPlayers", totalPlayers);
		modelMap.addAttribute("actualPlayer", actualPlayer);
		modelMap.addAttribute("shortest", shortest);
		modelMap.addAttribute("longuest", longuest);
		modelMap.addAttribute("factionMostWinned", factionMostWinned);
		return vista;
	}
	

	private void getGlobalEstadistics(Estadistics globalEstadistic2, List<Player> playerList) {
		for(Player player : playerList) {
			globalEstadistic2.setMatchsPlayed(player.getEstadistic().getMatchsPlayed()+globalEstadistic2.getMatchsPlayed());
			//PUNTOS EN TOTAL
			globalEstadistic2.setPoints(player.getEstadistic().getPoints()+globalEstadistic2.getPoints());
			//PARTIDAS GANADAS
			globalEstadistic2.setMatchsWins(player.getEstadistic().getMatchsWins()+globalEstadistic2.getMatchsWins());
			//PARTIDAS PERDIDAS
			globalEstadistic2.setMatchsLoses(player.getEstadistic().getMatchsLoses()+globalEstadistic2.getMatchsLoses());
			//WINSTREAK MAS ALTO
			if(player.getEstadistic().getWinsLongerStrike()>globalEstadistic2.getWinsLongerStrike()) {
				globalEstadistic2.setWinsLongerStrike(player.getEstadistic().getWinsLongerStrike());
			}
			if(player.getEstadistic().getRankingPos()== null) {
				player.getEstadistic().setRankingPos(playerList.indexOf(player)+1);
				estadisticsService.save(player.getEstadistic());
			}
		}
		
	}

	public void shorterDurationGE(List<Player> playerList,Estadistics globalEstadistics) {
		Comparator<Player> shorterMatch = (Player p1,Player p2) -> p1.getEstadistic().getMatchShorterDuration().compareTo(p2.getEstadistic().getMatchShorterDuration());
		playerList.sort(shorterMatch.reversed());
		globalEstadistics.setMatchShorterDuration(playerList.get(0).getEstadistic().getMatchShorterDuration());
	}
	
	public void longuestDurationGE(List<Player> playerList,Estadistics globalEstadistics) {
		Comparator<Player> longuestMatch = (Player p1,Player p2) -> p1.getEstadistic().getMatchLongerDuration().compareTo(p2.getEstadistic().getMatchLongerDuration());
		playerList.sort(longuestMatch.reversed());
		globalEstadistics.setMatchLongerDuration(playerList.get(0).getEstadistic().getMatchLongerDuration());
	}

	private List<Player> allPlayersInList() {
		Iterable<Player> playerIterable = playerService.findAll();
		List<Player> playerList = IteratorToStream.iterableToStream(playerIterable).filter(x->x.getEstadistic()!=null && x.getEstadistic().getMatchLongerDuration()!=null && x.getEstadistic().getMatchShorterDuration()!=null).collect(Collectors.toList());
		return playerList;
	}


	private List<Player> getTopTenRanking() {
		Iterable<Player> playerIterable = playerService.findAll();
		List<Player> playerList = IteratorToStream.iterableToStream(playerIterable).filter(x->x.getEstadistic()!=null).collect(Collectors.toList());
		Comparator<Player> comparatorPoints = (Player p1, Player p2) -> p1.getEstadistic().getPoints().compareTo(p2.getEstadistic().getPoints());
		playerList.sort(comparatorPoints.reversed());
		List<Player> topTenPlayers = playerList.stream().limit(10).collect(Collectors.toList());
		return topTenPlayers;
	}


	 private String IntegerAMinSegs(Integer matchLongerDuration) {
			int minutos = (int)Math.floor(matchLongerDuration/60);
			int segundos = (matchLongerDuration)-(minutos*60);
			String res = minutos + " minutos y " + segundos + " segundos.";
			return res;
		}

	 private String FaccionConMasPartidasGanadas() {
		 List<Match> matchList = matchService.findAllDesc().stream().filter(x->x.getWinner()!=null).collect(Collectors.toList());
		 int loyalPartidasGanadas = 0;
		 int merchantPartidasGanadas = 0;
		 int traitorPartidasGanadas = 0;
		 String res = "";
		 for(int i = 0; i < matchList.size();i++) {
			Faction winnerInThisMatch = matchList.get(i).getWinner();
			
			if(winnerInThisMatch == Faction.LOYAL) {
				loyalPartidasGanadas++;
			} else if (winnerInThisMatch == Faction.MERCHANT) {
				merchantPartidasGanadas++;
			} else if (winnerInThisMatch == Faction.TRAITOR) {
				traitorPartidasGanadas++;
			}
			
			
		 }	 
		 
		
		 if(loyalPartidasGanadas == merchantPartidasGanadas && merchantPartidasGanadas == traitorPartidasGanadas) {
			 res = "Las tres facciones han ganado el mismo número de veces";
		 } else if (loyalPartidasGanadas > merchantPartidasGanadas && loyalPartidasGanadas >traitorPartidasGanadas) {
			 res = "Facción Loyal";
		 } else if ( merchantPartidasGanadas > loyalPartidasGanadas && merchantPartidasGanadas >traitorPartidasGanadas) {
			 res = "Facción Merchant";
		 } else if ( traitorPartidasGanadas > loyalPartidasGanadas && traitorPartidasGanadas > merchantPartidasGanadas) {
			 res = "Facción Traitor";
		 }
		 return res;
		 
	 }

}
