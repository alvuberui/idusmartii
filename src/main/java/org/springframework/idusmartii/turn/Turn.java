package org.springframework.idusmartii.turn;

import lombok.Getter;
import lombok.Setter;
import org.springframework.idusmartii.consul.Consul;
import org.springframework.idusmartii.edil.Edil;
import org.springframework.idusmartii.edil.VOTE;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchTurnStatus;
import org.springframework.idusmartii.model.BaseEntity;
import org.springframework.idusmartii.pretor.Pretor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter

public class Turn extends BaseEntity {


    private LocalTime nextStateTurn;

    private MatchTurnStatus matchTurnStatus;


    @NotNull
    private Integer turn;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "match_id", referencedColumnName = "id")
    private Match match;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turn")
    private List<Edil> playerEdil;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "consul_id", referencedColumnName = "id")
    private Consul consul;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pretor_id", referencedColumnName = "id")
    private Pretor pretor;
    public Turn(){}


    public Turn (Match match, Integer turn){
        this.match = match;
        this.turn = turn;
    }


    public Turn(Match match,Integer turn, Consul consul){
        this.turn = turn;
        this.consul = consul;
        this.match = match;
    }

    @Override
    public String toString() {
        return "Turn{" +
            "nextStateTurn=" + nextStateTurn +
            ", matchTurnStatus=" + matchTurnStatus +
            ", turn=" + turn +
            ", match=" + match +
            ", playerEdil=" + playerEdil +
            ", consul=" + consul +
            ", pretor=" + pretor +
            '}';
    }

    public void nextStatusTurn(Integer time){
        List<MatchTurnStatus> statusList = Arrays.stream(MatchTurnStatus.values()).collect(Collectors.toList());
        if(statusList.size()-1 > statusList.indexOf(this.matchTurnStatus)){
            Integer i = statusList.indexOf(this.matchTurnStatus);
            //this.matchTurnStatus = statusList.get(statusList.size()-1);
            if(this.getMatchTurnStatus() == MatchTurnStatus.CHANGING_VOTE && (pretor.getEdil() == null || (pretor.getEdil() != null && pretor.getEdil().getVote() != VOTE.NEUTRAL))){
                this.matchTurnStatus = MatchTurnStatus.CONT;
            }else{
                this.matchTurnStatus = statusList.get(i+1);
            }



            System.out.println(this.matchTurnStatus);

        }
        this.TimerPlusTurn(time);
    }


    public void TimerPlusTurn(Integer time){this.nextStateTurn = LocalTime.now().plusSeconds(time);
    }


    public Integer getRound(){
        if(this.getTurn() < this.match.getNumPlayers()){
            return 1;
        }else  if(this.getTurn() >= this.match.getNumPlayers()){
            return 2;
        }else {
            return 0;
        }
    }

    public String toStringPretor(){
        return "El consul: " + this.getConsul().getPlayer().getUser().getUsername() + " ha elegido como pretor a" + this.getPretor().getPlayer().getUser().getUsername();
    }

    public String toStringEdil(Edil edil){
        return "El consul: " + this.getConsul().getPlayer().getUser().getUsername() + " ha elegido como edil " + edil.getPlayer().getUser().getUsername();
    }

    public List<String> toString2(){
        List<String> res = new ArrayList<>();
        List<Edil> ediles = this.getPlayerEdil();
        if(this.getRound() == 2){
            if(this.getPretor() != null){
                res.add(this.toStringPretor());
            }
            if(this.getPlayerEdil() != null){
                for(Edil edil : this.getPlayerEdil()){
                    res.add(this.toStringEdil(edil));
                }
            }
        }

        if(ediles != null && ediles.size() == 2){
            Edil edil1 = ediles.get(0);
            Edil edil2 = ediles.get(1);
            if(edil1.getVote() != null){
                res.add(edil1.toStringVote());
            }
            if(edil2.getVote() != null){
                res.add(edil1.toStringVote());
            }
        }
        if(this.getPretor() != null && this.getPretor().getEdil() != null){
            if(this.getRound() == 1){
                if(this.getPretor().getEdil().getVoteChange() != null){
                    res.add(this.getPretor().toStringEdil3());
                }
            }else{
                if(this.getPretor().getEdil().getVote() != VOTE.NEUTRAL){
                    res.add(this.getPretor().toStringEdil2());
                }else if(this.getPretor().getEdil().getVoteChange() != null){
                    res.add(this.getPretor().toStringEdil());
                }
            }
        }
        if(this.getConsul().getFaction() != null){
            res.add(this.getConsul().toStringFaction());
        }
        return res;
    }

}
