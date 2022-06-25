package org.springframework.idusmartii.pretor;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.springframework.idusmartii.edil.Edil;
import org.springframework.idusmartii.model.MatchTurnEntity;
import org.springframework.idusmartii.players.Player;

import lombok.Getter;
import lombok.Setter;




@Getter
@Setter
@Entity
public class Pretor extends MatchTurnEntity {


    @OneToOne
    @JoinColumn(name = "edil_id", referencedColumnName = "id")
    private Edil edil;

    public Pretor() {}

    public Pretor(Player player) {
        this.setPlayer(player);
    }

    public String toStringEdil(){
        return "El pretor: " + this.getPlayer().getUser().getUsername() + " ha elegido para cambiar su voto " + this.getEdil().getPlayer().getUser().getUsername();
    }

    public String toStringEdil2(){
        return "El pretor: " + this.getPlayer().getUser().getUsername() + " ha intentado cambiar el voto " + this.getEdil().getPlayer().getUser().getUsername();
    }

    public String toStringEdil3(){
        return "El pretor: " + this.getPlayer().getUser().getUsername() + " ha intercambiado el voto a " + this.getEdil().getPlayer().getUser().getUsername();
    }
}
