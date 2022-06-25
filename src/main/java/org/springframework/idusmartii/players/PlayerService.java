package org.springframework.idusmartii.players;




import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.UserService;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {


	@Autowired
	private PlayerRepository playerRepo;

	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	private UserService userService;
	
	//@Autowired
    //private SessionRegistry sessionRegistry;

	@Transactional
	public int playerCount() {
		return (int) playerRepo.count();
	}

	public Page<Player> getPage(Pageable pageable){
        return playerRepo.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Player> findPlayerById(int playerId) {
        return playerRepo.findById(playerId);
    }

	@Transactional
	public Iterable<Player> findAll() {
		return playerRepo.findAll();
	}

	/*
	 * alvuberui: busca los jugadores sentados en una mesa
	 * a través de la id de la mesa.
	 */
	@Transactional(readOnly = true)
	public Collection<Player> findPlayerByBoardId(int boardId) {
		return playerRepo.findByBoardId(boardId);
	}
	@Transactional
    public Player getByUsername(String username){
        return playerRepo.getByUsername(username);
    }
	@Transactional
	public void delete(Player player) {
		playerRepo.delete(player);
	}

	@Transactional
	public void create(Player player) {
		playerRepo.save(player);
	}

	@Transactional
	public void savePlayer(Player player) throws DataAccessException {
		//creating owner
		playerRepo.save(player);
		
		userService.saveUser(player.getUser());
		
		authoritiesService.saveAuthorities(player.getUser().getUsername(), "player");
	}

    /*
	 * alvuberui: busca los jugadores jugando una partida
	 * a través de la id de la partida.
	 */
	public Collection<Player> findPlayerByMatchId(int matchId) {
		return playerRepo.findByMatchId(matchId);
	}

    public List<Player> findByUsernameContaining(String username){return playerRepo.findByUserUsernameStartsWith(username);}
    
    /*
    public List<String> getPlayersFromSessionRegistry() {
        List<String> vars = sessionRegistry.getAllPrincipals().stream()
                .filter((u) -> !sessionRegistry.getAllSessions(u, false)
                        .isEmpty())
                    .map(o -> {
                        if (o instanceof Player) {
                            return ((Player) o).getUser().getUsername();
                        } else {
                            return o.toString()
                    ;
                        }
                    }).collect(Collectors.toList());
    	return vars;
    }
    */
}
