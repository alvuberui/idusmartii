package org.springframework.idusmartii.friends;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.idusmartii.friends.Friend;
import org.springframework.idusmartii.players.Player;

public interface FriendRepository extends CrudRepository<Friend, Integer>{
	
	@Query("Select f FROM Friend f where (f.player1 = :friendA OR f.player2 = :friendA) AND f.friendState = 1")
	  public Collection<Friend> getByFriendA(@Param("friendA") Player friendA);
	
	@Query("Select f FROM Friend f where f.player1 = :friendSA AND f.friendState = 1")
	  public Collection<Friend> getByFriendSA(@Param("friendSA") Player friendSA);
	
	@Query("Select f FROM Friend f where f.player2 = :friendRA AND f.friendState = 1")
	  public Collection<Friend> getByFriendRA(@Param("friendRA") Player friendRA);
	
	@Query("Select f FROM Friend f where f.player1 = :friendSP AND f.friendState = 0")
	  public Collection<Friend> getByFriendSP(@Param("friendSP") Player friendSP);
	
	@Query("Select f FROM Friend f where f.player2 = :friendRP AND f.friendState = 0")
	  public Collection<Friend> getByFriendRP(@Param("friendRP") Player friendRP);

	

	
}
