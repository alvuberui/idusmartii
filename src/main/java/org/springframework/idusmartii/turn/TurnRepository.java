package org.springframework.idusmartii.turn;

import org.springframework.idusmartii.match.Match;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface TurnRepository extends CrudRepository<Turn, Integer> {
    Collection<Turn> getTurnByMatch(Match match);
}
