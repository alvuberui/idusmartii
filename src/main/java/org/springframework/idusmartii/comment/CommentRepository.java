package org.springframework.idusmartii.comment;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.idusmartii.match.Match;
import org.springframework.stereotype.Repository;
@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {
	
	@Query("Select m FROM Comment m WHERE m.match.id = :matchId ORDER BY id DESC")
	Iterable<Comment> findCommentsByMatchId(int matchId);
	
	@Query("Select m FROM Comment m ORDER BY id DESC")
	public List<Match> getAllDesc();
}
