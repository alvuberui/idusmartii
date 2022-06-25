package org.springframework.idusmartii.board;



import javax.persistence.*;


import java.util.List;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.model.BaseEntity;
import org.springframework.idusmartii.players.Player;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Board extends BaseEntity {

	/*
	 * alvuberui: La única propiedad de mesa es la id,
	 * heredada de baseEntity
	 */
	@Max(8)
	@NotNull
	private Integer numPlayers = 0;


	@OneToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "anfitrion_id", referencedColumnName = "id")
    private Player anfitrion;

    @ManyToOne
    @JoinColumn(name = "matchplaying", referencedColumnName = "id")
    private Match playingMatch;


	 public Board(@NotNull Integer numPlayers) {
			super();
			this.numPlayers = numPlayers;
     }

	 public Board(@NotNull List<Player> players) {
			super();
			this.numPlayers = players.size();
     }

	 public Board() {
			super();
     }
}
