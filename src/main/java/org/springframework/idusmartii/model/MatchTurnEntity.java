package org.springframework.idusmartii.model;

import org.springframework.idusmartii.players.Player;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;


@MappedSuperclass
public class MatchTurnEntity extends BaseEntity{

	@NotNull
    @ManyToOne
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    protected Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
