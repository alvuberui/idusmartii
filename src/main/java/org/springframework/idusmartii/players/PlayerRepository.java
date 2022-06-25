package org.springframework.idusmartii.players;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;


public interface PlayerRepository extends CrudRepository<Player, Integer>, PagingAndSortingRepository<Player, Integer> {

  @Query("Select p FROM Player p where p.user.username = :username")
  public Player getByUsername(@Param("username") String username);

	/*
	 * alvuberui: busca los jugadores sentados en la mesa, con mesaid = boardid
	 */
	@Query("SELECT player FROM Player player where player.board.id=:board_id")
	public Collection<Player> findByBoardId(int board_id);

	/*
	 * alvuberui: busca los jugadores jugando una partida, con id = matchid
	 */
	@Query("SELECT player FROM Player player where player.match.id=:match_id")
	public Collection<Player> findByMatchId(int match_id);


    public List<Player> findByUserUsernameStartsWith(String username);


    //public Page<Player> findAllPageable(Pageable pageable);

}
