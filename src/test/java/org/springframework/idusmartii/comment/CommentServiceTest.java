package org.springframework.idusmartii.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.idusmartii.board.Board;
import org.springframework.idusmartii.board.BoardService;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchService;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CommentServiceTest {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private AuthoritiesService authoritiesService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private  CommentService commentService;
	
	@Autowired
	private MatchService matchService;
	
    @Test
	@Transactional
	public void boardServiceTestComplete() throws Exception {
    	List<Player> players = auxCreateplayers(5);
	
		// ---- BOARD ---- //
		Board board = new Board();
		board.setAnfitrion(players.get(0));
		board.setNumPlayers(players.size());
		
		try {
            this.boardService.saveBoard(board);
        } catch (Exception ex) {
        	throw new Exception("La mesa con id " + board.getId() + " no ha podido ser guardada");
        }
		
		Match match = new Match(players);
		match.setBoard(board);
		
        try {
            this.matchService.save(match);
        } catch (Exception ex) {
            throw new Exception("La partida con id " + match.getId() + " no ha podido ser guardada");
        }
        
        Comment comment = new Comment();
        comment.setComment("Comentario nuevo");
        comment.setDate(LocalDateTime.now());
        comment.setMatch(match);
        comment.setPlayer(players.get(0));
        
        try {
            this.commentService.save(comment);
        } catch (Exception ex) {
            throw new Exception("El comentario no ha podido ser guardada");
        }
        List<Comment> comments = (List<Comment>) commentService.findAll();
        Integer commentId = comment.getId();
        assertEquals(comment, commentService.findCommentById(commentId).get());
        List<Comment> commentsbymatch = (List<Comment>) commentService.findCommentsByMatchId(match.getId());
        assertThat(comment.equals(commentsbymatch.get(0)));
        assertThat(comments.contains(comment));
    }
    
    
    @Test
	@Transactional
	public void nullProperty() throws Exception {
    	List<Player> players = auxCreateplayers(5);
	
		// ---- BOARD ---- //
		Board board = new Board();
		board.setAnfitrion(players.get(0));
		board.setNumPlayers(players.size());
		
		try {
            this.boardService.saveBoard(board);
        } catch (Exception ex) {
        	throw new Exception("La mesa con id " + board.getId() + " no ha podido ser guardada");
        }
		
		Match match = new Match(players);
		match.setBoard(board);
		
        try {
            this.matchService.save(match);
        } catch (Exception ex) {
            throw new Exception("La partida con id " + match.getId() + " no ha podido ser guardada");
        }
        
        Comment comment = new Comment();
        comment.setComment("Comentario nuevo");
        comment.setDate(null);
        comment.setMatch(match);
        comment.setPlayer(players.get(0));
        
        Assertions.assertThrows(javax.validation.ConstraintViolationException.class, () ->{
        	this.commentService.save(comment);
    	});	
    }
    
    
    
    
    
	/*
	 * alvuberui: método auxiliar para crear n players automáticamente
	 */
	@Transactional
	public List<Player> auxCreateplayers(Integer numPlayers) throws Exception {
		List<Player> players = new ArrayList<Player>();
		
		for(int acum=0; acum < numPlayers; acum++) {
			Player testPlayer = new Player();
			testPlayer.setFirstName("prueba"+acum);
			testPlayer.setLastName("test"+acum);
			testPlayer.setEmail("prueba"+acum+"@gmail.com");
			testPlayer.setUser(null);
			
			User testUser = new User();
		    testUser.setUsername("testUser"+acum);
		    testUser.setPassword("supersecretpassword"+acum);
		    testUser.setEnabled(true);
			testUser.setUsername("testUser"+acum);
			
			testPlayer.setUser(testUser);
			
			try {
				this.playerService.savePlayer(testPlayer);
			} catch (Exception CantSavePlayer) {
				throw new Exception("Hay un problema al guardar el player");
			}
			
			try {
				authoritiesService.saveAuthorities("testUser"+acum,"player");
			} catch (Exception CantSaveAutorithies) {
				throw new Exception("Hay un problema al guardar las autorithies");
			}
			
			try {
				userService.saveUser(testUser);
			} catch (Exception CantSavePlayer) {
				throw new Exception("Hay un problema al guardar el user");
			}
			players.add(testPlayer);
		}
		return players;
	}
	
}
