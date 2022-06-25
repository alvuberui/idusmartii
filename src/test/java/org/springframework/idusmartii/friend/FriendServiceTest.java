package org.springframework.idusmartii.friend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.idusmartii.friends.Friend;
import org.springframework.idusmartii.friends.FriendService;
import org.springframework.idusmartii.friends.FriendState;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class FriendServiceTest {


	@Autowired
	protected FriendService friendService;

	@Autowired
	protected AuthoritiesService authoritiesService;

	@Autowired
	protected UserService userService;


	@Autowired
	protected PlayerService playerService;


	@Transactional
	public List<Player> auxCreateplayersForFriends(Integer numPlayers) throws Exception {
		List<Player> players = new ArrayList<Player>();

		for(int acum=0; acum < numPlayers; acum++) {
			Player testPlayer = new Player();
			testPlayer.setFirstName("pruebaFriend"+acum);
			testPlayer.setLastName("testFriend"+acum);
			testPlayer.setEmail("pruebaFriend"+acum+"@gmail.com");
			testPlayer.setUser(null);

			User testUser = new User();
		    testUser.setUsername("testUserFriend"+acum);
		    testUser.setPassword("supersecretpasswordFriend"+acum);
		    testUser.setEnabled(true);
			testUser.setUsername("testUserFriend"+acum);

			testPlayer.setUser(testUser);

			try {
				this.playerService.savePlayer(testPlayer);
			} catch (Exception CantSavePlayer) {
				throw new Exception("Hay un problema al guardar el player");
			}

			try {
				authoritiesService.saveAuthorities("testUserFriend"+acum,"player");
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

	@Test
	@Transactional
	public void FriendAdd() throws Exception {
		List<Player>LP=auxCreateplayersForFriends(3);
		Player p1=LP.get(0);
		Player p2=LP.get(1);
		Friend f=new Friend();
		FriendState FS=FriendState.ACCEPTED;
		f.setPlayer1(p1);
		f.setPlayer2(p2);
		f.setFriendState(FS);

		 try {
	            this.friendService.saveFriend(f);
	        } catch (Exception ex) {
             throw new Exception("El amigo no ha podido ser guardado");
	        }
		 Collection<Friend> friendC1 = (Collection<Friend>) friendService.findAll();
         assertThat(friendC1.contains(f));//Asserting that the friend is created as intended
        assertThat(f.getId()).isNotNull();
	}
	@Test
	@Transactional
	public void FriendDelete() throws Exception{
		List<Player>LP=auxCreateplayersForFriends(4);
		Player p1=LP.get(2);
		Player p2=LP.get(3);
		Friend f=new Friend();
		FriendState FS=FriendState.ACCEPTED;
		f.setPlayer1(p1);
		f.setPlayer2(p2);
		f.setFriendState(FS);

		 try {
	            this.friendService.saveFriend(f);
         } catch (Exception ex) {
             throw new Exception("El amigo no ha podido ser guardado");
         }
		 Collection<Friend> friendC1 = (Collection<Friend>) friendService.findAll();
			assertThat(friendC1.contains(f));//Asserting that the friend is created as intended
            assertThat(f.getId()).isNotNull();
		 Integer fid=f.getId();
		 try {
			 this.friendService.delete(f);
		 } catch(Exception ex) {
			 System.out.println("El amigo con id " + f.getId() + " no ha podido ser eliminado");
		 }
		 Collection<Friend> friendC2 = (Collection<Friend>) friendService.findAll();
			assertThat(!friendC2.contains(f)).isTrue();//Asegurar que se ha borrado
	}
	@Test
	@Transactional
	public void FriendCheckMethods() throws Exception{
		List<Player>LP=auxCreateplayersForFriends(11);
		Player p1=LP.get(8);
		Player p2=LP.get(9);
		Friend f=new Friend();
		FriendState FS1=FriendState.ACCEPTED;
		f.setPlayer1(p1);
		f.setPlayer2(p2);
		f.setFriendState(FS1);
		int FSC=friendService.friendCount();
		try {
            this.friendService.saveFriend(f);
        } catch (Exception ex) {
            System.out.println("El amigo con id " + f.getId() + " no ha podido ser guardado");
        }
		Collection<Friend> friendC1 = (Collection<Friend>) friendService.findAll();
		assertThat(friendC1.contains(f));//Asserting that the friend is created as intended
		//---Count---//
		assertThat(friendC1.size()==FSC+1);
		//---FindFriendById---//
		assertThat(friendService.findFriendById(f.getId())
				.get()==f);
		//---FindFriendByReceiver---//
		assertThat(friendService.findFriendByReceiver(p1).stream()
				.allMatch(fri->fri.getPlayer2()==f.getPlayer1()));
		//---FindFriendOf---//
		assertThat(friendService.findFriendOfPlayer(p1).stream()
				.allMatch(fri->fri.getPlayer1()==f.getPlayer1() ||fri.getPlayer2()==f.getPlayer1()));

	}
	@Test
	@Transactional
	public void FriendAddDuplicate() throws Exception {
		List<Player>LP=auxCreateplayersForFriends(8);
		Player p1=LP.get(4);
		Player p2=LP.get(5);
		Friend f=new Friend();
		Friend f2=new Friend();
		FriendState FS1=FriendState.ACCEPTED;
		FriendState FS2=FriendState.PENDING;
		f.setPlayer1(p1);
		f.setPlayer2(p2);
		f.setFriendState(FS1);
		f2.setPlayer1(p1);
		f2.setPlayer2(p2);
		f2.setFriendState(FS2);


		 try {
	            this.friendService.saveFriend(f);
	        } catch (Exception ex) {
	            System.out.println("El amigo con id " + f.getId() + " no ha podido ser guardado");
	        }
		 Collection<Friend> friendC1 = (Collection<Friend>) friendService.findAll();
			assertThat(friendC1.contains(f));//Asserting that the friend is created as intended
			try {
	            this.friendService.saveFriend(f2);
	        } catch (Exception ex) {
	            System.out.println("El amigo con id " + f.getId() + " no ha podido ser guardado");
	        }


	}
}

