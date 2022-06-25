package org.springframework.idusmartii.consul;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.springframework.idusmartii.faction.FactionCard;
import org.springframework.idusmartii.model.MatchTurnEntity;
import org.springframework.idusmartii.players.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Consul extends MatchTurnEntity{

    @OneToOne
    private FactionCard faction;

    public Consul() {}

    public Consul(Player player) {
        this.setPlayer(player);
    }

    public String toStringFaction(){
        return "El consul: " + this.getPlayer().getUser().getUsername() + "ha elegido faccion.";
    }
}
