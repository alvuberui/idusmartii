package org.springframework.idusmartii.edil;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.springframework.idusmartii.model.MatchTurnEntity;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.turn.Turn;


@Getter
@Setter
@Entity
public class Edil extends MatchTurnEntity {

	@NotNull
    @ManyToOne
    @JoinColumn(name = "turn_id", referencedColumnName = "id")
    private Turn turn;

    private VOTE vote;

    private VOTE voteChange;

    public Edil(){}

    public Edil(Turn turn, Player player){
        this.setPlayer(player);
        this.turn = turn;
    }

    public String toStringVote(){
        return "El edil: " + this.player.getUser().getUsername() + " ha votado.";
    }


}
