package org.springframework.idusmartii.players;




import javax.persistence.*;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import javax.validation.constraints.NotEmpty;


import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.idusmartii.achievement.Achivement;
import org.springframework.idusmartii.estadistics.Estadistics;
import org.springframework.idusmartii.friends.Friend;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.idusmartii.board.Board;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.model.Person;
import org.springframework.idusmartii.user.User;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Audited

@Table(uniqueConstraints={
    @UniqueConstraint(name="uniqueUsername", columnNames={"username"})
})
public class Player extends Person {

    @NotAudited
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
	private User user;

//	@EmailConstraint
	private String email;

	private Boolean playing;

    @NotAudited
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estadisticId", referencedColumnName = "id")
	private Estadistics estadistic;

    @NotAudited
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "boardId", referencedColumnName = "id")
	private Board board;

    @NotAudited
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "matchsPlayed", joinColumns = @JoinColumn(name = "playerId"),inverseJoinColumns = @JoinColumn(name = "matchId"))
    private Set<Match> matchsPlayed;

    @NotAudited
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "matchId", referencedColumnName = "id")
	private Match match;


    @Override
    public String toString() {
        return "Player{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", user=" + user +
            ", email='" + email + '\'' +
            ", board=" + board +
            ", match=" + match +
            ", estadistics=" + estadistic +
            '}';
    }



}
