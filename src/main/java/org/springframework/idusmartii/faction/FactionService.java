package org.springframework.idusmartii.faction;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.players.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FactionService {

	@Autowired
	private FactionRepository factionRepo;

	@Transactional
	public int voteCount() { //Testeado
		return (int) factionRepo.count();
	}

	@Transactional //Testeado
	public Iterable<FactionCard> findAll() {
		return factionRepo.findAll();
	}

    public Collection<FactionCard> findByMatchAndPlayer(Match match, Player player){ //Testeado
        return factionRepo.findByMatchAndPlayer(match, player);
    }

	@Transactional //Testeado
	public void save(FactionCard FactionCard) {
		factionRepo.save(FactionCard);

	}

	public void delete(FactionCard FactionCard) { //Testeado
		factionRepo.delete(FactionCard);

	}

	@Transactional(readOnly=true)
	public Optional<FactionCard> findFactionCardById(int FactionCardId) { //Testeado
		return factionRepo.findById(FactionCardId);
	}

}
