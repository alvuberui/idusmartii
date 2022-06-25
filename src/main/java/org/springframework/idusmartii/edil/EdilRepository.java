package org.springframework.idusmartii.edil;

import org.springframework.data.repository.CrudRepository;
import org.springframework.idusmartii.turn.Turn;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface EdilRepository extends CrudRepository<Edil, Integer>{
    Optional<Edil> findEdilById(Integer id);

    List<Edil> getEdilByTurn(Turn turn);
}
