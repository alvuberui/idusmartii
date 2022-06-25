package org.springframework.idusmartii.match;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.board.Board;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchService {

	private MatchRepository matchRepo;
	
	@Autowired
	public MatchService(MatchRepository matchRepository) {
		this.matchRepo = matchRepository;
	}
	
	@Transactional
	public int matchCount() { //Testeado
		return (int) matchRepo.count();
	}

	@Transactional
	public Iterable<Match> findAll() { //Testeado
		return matchRepo.findAll();
	}

	@Transactional(readOnly=true)
	public Optional<Match> findMatchById(int matchId) { //Testeado
		return matchRepo.findById(matchId);
	}

    @Transactional
    public void save(Match m) { //Testeado
        matchRepo.save(m);
    }

    @Transactional
    public Match getMatchBoardId(Board board) { //Testeado
        return matchRepo.getMatchByBoard(board);
    }

	public List<Match> findAllDesc() {
		return matchRepo.getAllDesc();
	}
    
//    @Transactional
//    public Iterable<Match> getMatchsOrderedLimited() { //Testeado
//        return matchRepo.getMatchsOrderedLimited();
//    }
}
