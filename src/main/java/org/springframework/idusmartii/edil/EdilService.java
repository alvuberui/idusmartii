package org.springframework.idusmartii.edil;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.turn.Turn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EdilService {

	@Autowired
	private EdilRepository voteRepo;

	@Transactional
	public int voteCount() {
		return (int) voteRepo.count();
	}

	@Transactional
	public Iterable<Edil> findAll() {
		return voteRepo.findAll();
	}

	@Transactional
	public void save(Edil edil) {
		voteRepo.save(edil);

	}
    @Transactional()
	public void delete(Edil edil) {
		voteRepo.delete(edil);

	}
	@Transactional(readOnly=true)
	public Optional<Edil> findById(int edilId) {
		return voteRepo.findById(edilId);
	}
    public List<Edil> getByTurn(Turn turn) { return voteRepo.getEdilByTurn(turn); }
}
