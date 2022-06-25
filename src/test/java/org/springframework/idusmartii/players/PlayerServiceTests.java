package org.springframework.idusmartii.players;




import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PlayerServiceTests {

	@Autowired
	private  PlayerService playerService;

	@Autowired
	private AuthoritiesService authoritiesService;
	
	@Autowired
	private UserService userService;


	@Test
	@Transactional
	public void shouldFindPlayerById() {
		Optional<Player> player = this.playerService.findPlayerById(4);
		String playerName = player.get().getUser().getUsername();
		assertEquals(playerName,"alvaro_ubeda8","The user with ID is not alvaro_ubeda8");
	}


	
	



	

}
