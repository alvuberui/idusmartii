package org.springframework.idusmartii.faction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.players.Player;

import java.util.Collection;



public interface FactionRepository extends CrudRepository<FactionCard, Integer>{


    Collection<FactionCard> findByMatchAndPlayer(Match match, Player player);


}
