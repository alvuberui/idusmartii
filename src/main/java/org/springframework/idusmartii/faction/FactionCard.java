package org.springframework.idusmartii.faction;

import javax.persistence.*;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.model.BaseEntity;
import org.springframework.idusmartii.players.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cards")
public class FactionCard extends  BaseEntity{

    @ManyToOne
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "match_id", referencedColumnName = "id")
    private Match match;


    private Boolean voted;

	private Faction cardType;


    public FactionCard (){}

    @Override
    public String toString() {
        return "FactionCard{" +
            "player=" + player +
            ", match=" + match +
            ", voted=" + voted +
            ", cardType=" + cardType +
            '}';
    }

    public FactionCard (Player player, Match match, Faction cardType){
        this.player = player;
        this.match = match;
        this.cardType = cardType;
        this.voted = false;
    }
}
