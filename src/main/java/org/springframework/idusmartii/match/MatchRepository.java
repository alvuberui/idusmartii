package org.springframework.idusmartii.match;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.idusmartii.board.Board;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends CrudRepository<Match, Integer>{
	
	
    public Match getMatchByBoard(Board board);
    
 

    @Query("Select m FROM Match m ORDER BY id DESC")
	public List<Match> getAllDesc();
}
