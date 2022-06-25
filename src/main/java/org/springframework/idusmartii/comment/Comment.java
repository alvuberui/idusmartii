package org.springframework.idusmartii.comment;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
public class Comment extends BaseEntity {
	
	@NotNull
	private String comment;
	
	@NotNull
	private LocalDateTime date;
	
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "player_id", referencedColumnName = "id")
	private Player player;
	
	@ManyToOne
	@JoinColumn(name = "match_id", referencedColumnName = "id")
	private Match match;
	
	
}
