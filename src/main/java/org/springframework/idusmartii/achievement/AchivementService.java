package org.springframework.idusmartii.achievement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.players.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AchivementService {

	@Autowired
	private AchivementRepository achivementRepo;

	@Transactional(readOnly = true)
	public Iterable<Achivement> findAll() {
		return achivementRepo.findAll();
	}
	
	@Transactional
	public void delete(Achivement achivement) {
		achivementRepo.delete(achivement);
	}
	
	@Transactional(readOnly = true)
    public Optional<Achivement> findAchivementById(int achivementId) {
        return achivementRepo.findById(achivementId);
    }
	
	@Transactional
	public void create(Achivement achivement) {
		achivementRepo.save(achivement);
	}

}
