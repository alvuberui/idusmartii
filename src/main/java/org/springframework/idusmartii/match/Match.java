package org.springframework.idusmartii.match;


import javax.persistence.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.idusmartii.consul.Consul;
import org.springframework.idusmartii.edil.Edil;
import org.springframework.idusmartii.edil.VOTE;
import org.springframework.idusmartii.faction.Faction;
import org.springframework.idusmartii.faction.FactionCard;
import org.springframework.idusmartii.model.BaseEntity;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.Rol;
import org.springframework.idusmartii.board.Board;
import org.springframework.idusmartii.comment.Comment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.idusmartii.pretor.Pretor;

import org.springframework.idusmartii.turn.Turn;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@Setter
@Entity
public class Match extends BaseEntity {

	@Column
    private LocalDateTime startMatch;


	private Faction winner;

	@Max(8)
	@Min(5)
	private Integer numPlayers;

	@NotNull
	private MatchStatus matchStatus;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "match")
    private List<Comment> commentList;


	@NotNull
    @ManyToOne
	@JoinColumn(name = "boardId", referencedColumnName = "id")
	private Board board;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "match")
    private List<FactionCard> factionList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "match")
    private List<Player> playerList;


    @ManyToMany(mappedBy = "matchsPlayed")
    Set<Player> players;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "match")
    private List<Turn> turnList;

    @NotNull
    private Integer votosLeal;

    @NotNull
    private Integer votosTraidor;

    public Match() {}


    public Match(Integer i) {
        this.votosLeal=0;
        this.votosTraidor=0;
        this.winner = null;
        this.numPlayers = i;
        this.matchStatus = MatchStatus.WAITTING;
        this.startMatch = LocalDateTime.of(LocalDate.now(), LocalTime.now().plusSeconds(1));
    }

    public Match(List<Player> players) {

        this.votosLeal=0;
        this.votosTraidor=0;
        this.winner = null;
        this.numPlayers = players.size();
        this.matchStatus = MatchStatus.WAITTING;
        this.playerList = players;

        this.startMatch = LocalDateTime.of(LocalDate.now(), LocalTime.now().plusSeconds(1));
    }

    public List<Faction> generarBarajaRandom(){
        List<Faction> r = new ArrayList<>();
        for(int i = 0; i<(this.numPlayers-1); i++){
            r.add(Faction.LOYAL); r.add(Faction.TRAITOR);
        }
        r.add(Faction.MERCHANT); r.add(Faction.MERCHANT);
        Collections.shuffle(r);
        return r;
    }
    public Turn makeTurn(Turn turn, Integer time){
        System.out.println("Making turn: " + turn.getTurn());
        //En la primera ronda los roles son generados automaticamente
        System.out.println("On Round: " + getRound(turn));

        LocalTime tiempo = LocalTime.now().plusSeconds(time);
        turn.setNextStateTurn(tiempo);
        if(turn.getRound() == 1){
            turn = getTurnWithRols(turn);
            turn.setMatchTurnStatus(MatchTurnStatus.WAITTOVOTE);
        }else if(turn.getRound() == 2){
            turn.setMatchTurnStatus(MatchTurnStatus.CHOOSE_ROL);
        }
        System.out.println(turn);
        return turn;
    }
    public HashMap<Player, Rol> ChangeRolFirstRound(Integer ConsulPlayerIndex){
      List<Player> playersList = this.getPlayerList().stream().sorted(Comparator.comparing(Player::getId)).collect(Collectors.toList());
      Collections.shuffle(playersList, new Random(this.getId()));
      HashMap<Player, Rol> m = new HashMap<>();
      //para el Garro: Probar la funcion de MatchUtils
      if(ConsulPlayerIndex != null){
          for(int i = 0;i<playersList.size(); i++){
              Player player = playersList.get(i);
              Rol role = null;
              if( i==(ConsulPlayerIndex)%playersList.size() || i==(ConsulPlayerIndex+1)%playersList.size()) {
                  role = Rol.EDIL;
              }else if(i==(ConsulPlayerIndex+2)%playersList.size()) {
                  role = Rol.PRETOR;
              }
              else if(i==(ConsulPlayerIndex+3)%playersList.size()) {
                  role = Rol.CONSUL;
              }
              m.put(player, role);
          }
      }
      return m;
  }
    public Integer getRound(Turn turn){
        if(turn.getTurn() < this.getNumPlayers()){
            return 1;
        }else  if(turn.getTurn() >= this.getNumPlayers()){
            return 2;
        }else {
            return 0;
        }
  }
    public void cont(Turn turn){
        List<VOTE> edilVotes = turn.getPlayerEdil().stream().map(x-> x.getVote()).collect(Collectors.toList());
        VOTE vote1 = edilVotes.get(0);
        VOTE vote2 = edilVotes.get(1);

        if(vote1 != null || vote2 != null ){
            if(vote1 == VOTE.LOYAL){
                this.votosLeal += 1;
            }else if(vote1 == VOTE.TRAITOR){
                this.votosTraidor += 1;
            }
        }

  }
    public Turn getTurnWithRols(Turn turn){
    HashMap<Player, Rol> playersRol = ChangeRolFirstRound(turn.getTurn()); // Al turno se le suma la id del match de esta forma aunque se esten los mismo jugadores en otro match el consul sera otro
    List<Edil> edilList = new ArrayList<>();
    for(Player p : playersRol.keySet()){
        Rol rolePlayer = playersRol.get(p);
        if(rolePlayer == Rol.CONSUL){
            Consul consul = new Consul(p);
            turn.setConsul(consul);
        }else if(rolePlayer == Rol.PRETOR){
            Pretor pretor = new Pretor(p);
            turn.setPretor(pretor);
        }else if(rolePlayer== Rol.EDIL){
            edilList.add(new Edil(turn, p));
        }
    }
        turn.setPlayerEdil(edilList);
        return turn;
    }


    public List<String> getLogOfMatch(){
        List<String> res = new ArrayList<>();
        List<Turn> orderedList = this.getTurnList().stream().sorted().collect(Collectors.toList());
        for(Turn turn : orderedList){
            res.addAll(turn.toString2());
        }
        return res;
    }

    public  List<Player>  getNextPretorList(List<Player> playerWithNoRol){
        Turn lastTurn = this.getTurnList().stream().sorted(Comparator.comparing(Turn::getTurn)).collect(Collectors.toList()).get(this.getTurnList().size()-2);
        playerWithNoRol.remove(lastTurn.getPretor().getPlayer());
        return playerWithNoRol;
    }

    public  List<Player>  getNextEdilList(List<Player> playerWithNoRol){
        Turn lastTurn = this.getTurnList().stream().sorted(Comparator.comparing(Turn::getTurn)).collect(Collectors.toList()).get(this.getTurnList().size()-2);
        if(this.getNumPlayers() == 5){
            return playerWithNoRol;
        }
        playerWithNoRol.removeAll(lastTurn.getPlayerEdil().stream().map(Edil::getPlayer).collect(Collectors.toList()));
        return playerWithNoRol;
    }

}
