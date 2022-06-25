package org.springframework.idusmartii.friends;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.idusmartii.model.BaseEntity;
import org.springframework.idusmartii.players.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "friends", uniqueConstraints={
        @UniqueConstraint( name = "idx_col1_col2",  columnNames ={"player1","player2"})
     })
public class Friend extends BaseEntity{
	
	
	@ManyToOne()
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "Player1", referencedColumnName = "id")
	private Player player1;

	@ManyToOne()
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "Player2", referencedColumnName = "id")
	private Player player2;
	
	private FriendState friendState;
	
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "receiver", referencedColumnName = "id")
	private Player receiver;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "sender", referencedColumnName = "id")
	private Player sender;
	
	
	 @Override
	    public String toString() {
	        return "Friend{" +
	            "id=" + id +
	            ", Player1='" + player1 + '\'' +
	            ", Player2='" + player2 + '\'' +
	            ", FriendState='" + friendState + '\'' +
	            '}';
	    }

}
