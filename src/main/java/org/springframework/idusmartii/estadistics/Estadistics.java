package org.springframework.idusmartii.estadistics;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.idusmartii.achievement.Achivement;
import org.springframework.idusmartii.model.BaseEntity;
import org.springframework.idusmartii.players.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Estadistics extends BaseEntity {

	@OneToOne(mappedBy = "estadistic")
    private Player player;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "estadistics_achivements", joinColumns = @JoinColumn(name = "estadistic_id"),
			inverseJoinColumns = @JoinColumn(name = "achivement_id"))
    private List<Achivement> achivementList;

	/*
	 * Experiencia en puntos totales
	 */
	@NotNull
	private Integer points = 0;



	@Min(1)
	private Integer rankingPos = null;


	@NotNull
	private Integer matchsPlayed = 0;

	@NotNull
	private Integer matchsWins = 0;

	@NotNull
	private Integer matchsLoses = 0;

	/*
	 * Rachas de victoria mas largas, ej 3 partidas ganadas seguidas.
	 */
	@NotNull
	private Integer winsLongerStrike = 0;
	
	private Integer matchLongerDuration = null;
	
	private Integer matchShorterDuration = null;
	
	@NotNull
	private Integer actualWinStrike = 0;



    public void addAchivement(Achivement achivement){
        if(this.achivementList == null){
            this.achivementList = new ArrayList<>();
        }

        this.achivementList.add(achivement);
    }


}
