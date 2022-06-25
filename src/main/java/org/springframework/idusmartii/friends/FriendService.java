package org.springframework.idusmartii.friends;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.idusmartii.players.Player;
//import org.springframework.samples.petclinic.players.Player;
//import org.springframework.samples.petclinic.players.PlayerRepository;
//import org.springframework.samples.petclinic.vote.Vote;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendService {

	@Autowired
	private FriendRepository friendRepo;

	@Transactional
	public int friendCount() {
		return (int) friendRepo.count();
	}

    @Transactional(readOnly = true)
    public Optional<Friend> findFriendById(int friendId) {
        return friendRepo.findById(friendId);
    }

	@Transactional
	public Iterable<Friend> findAll() {
		return friendRepo.findAll();
	}

	@Transactional
	public void saveFriend(Friend friend) {friendRepo.save(friend);}

	public void delete(Friend friend) {friendRepo.delete(friend);}

	public Collection<Friend> findFriendByReceiver(Player reciever) {
		return friendRepo.getByFriendRP(reciever);
	}

	public Collection<Friend> findFriendOfPlayer(Player player) {
		return friendRepo.getByFriendA(player);
	}

    public void updateFriend(Friend friend) throws DataAccessException {
        friendRepo.save(friend);
    }

}
