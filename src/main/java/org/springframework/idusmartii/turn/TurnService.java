package org.springframework.idusmartii.turn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.match.Match;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;


@Service
public class TurnService {

    @Autowired
    private TurnRepository turnRepo;

    @Transactional 
	public int turnCount() { //Testeado
		return (int) turnRepo.count();
	}

    @Transactional
	public Iterable<Turn> findAll() { //Testeado
		return turnRepo.findAll();
	}

    @Transactional
	public void save(Turn turn) { //Testeado
		turnRepo.save(turn);

	}

    public void delete(Turn turn) { //Testeado
		turnRepo.delete(turn);

	}

    @Transactional(readOnly=true)
	public Optional<Turn> findTurnById(int turnId) { //Testeado
		return turnRepo.findById(turnId);
	}

    @Transactional
    public Collection<Turn> findTurnByMatch(Match match) { //Testeado
        return turnRepo.getTurnByMatch(match);
    }

}
