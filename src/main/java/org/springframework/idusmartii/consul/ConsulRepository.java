package org.springframework.idusmartii.consul;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.players.Player;

import java.util.Optional;

public interface ConsulRepository extends CrudRepository<Consul, Integer>{


    @Query("Select DISTINCT c FROM Turn t join t.consul c Where t.match = ?1 AND c.player = ?2")
    Optional<Consul> getConsulByPlayer(Match match, Player player);
}
