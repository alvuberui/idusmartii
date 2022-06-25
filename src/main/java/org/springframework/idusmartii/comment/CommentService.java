package org.springframework.idusmartii.comment;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
	
private CommentRepository commentRepo;
	
	@Autowired
	public CommentService(CommentRepository commentRepository) {
		this.commentRepo = commentRepository;
	}
	
	@Transactional
	public Iterable<Comment> findAll() { //Testeado
		return commentRepo.findAll();
	}

	@Transactional(readOnly=true)
	public Optional<Comment> findCommentById(int commentId) { //Testeado
		return commentRepo.findById(commentId);
	}
	
	@Transactional(readOnly=true)
	public Iterable<Comment> findCommentsByMatchId(int matchId) { //Testeado
		return commentRepo.findCommentsByMatchId(matchId);
	}

    @Transactional
    public void save(Comment m) { //Testeado
        commentRepo.save(m);
    }
    

    	
    
    
   
}
