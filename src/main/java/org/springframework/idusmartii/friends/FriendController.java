package org.springframework.idusmartii.friends;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.idusmartii.board.Board;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.user.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FriendController {

    @Autowired
    private JavaMailSender mailSender;

	@Autowired
	private FriendService friendService;

	@Autowired
	private PlayerService playerService;

	@GetMapping("/friendList")
	public String listFriend(ModelMap modelMap) {
		String vista = "friends/friendList";
		//Iterable<Friend>  friend = friendService.findAll();
		//modelMap.addAttribute("friends", friend);
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if(a!=null){
        	if(a.getPrincipal().toString() != "anonymousUser"){
        		Player p = playerService.getByUsername(a.getName());
        		Iterable<Friend>  friend = friendService.findFriendOfPlayer(p);
        		modelMap.addAttribute("friends", friend);
        		Iterable<Friend>  friendPending = friendService.findFriendByReceiver(p);

        		modelMap.addAttribute("friendsPending", friendPending);
        		List<Friend> LFC=new ArrayList<>();
        		for(Friend frin:friend) {
        			Player pact=new Player();
        			if(frin.getPlayer1().equals(p)) {
        				pact=frin.getPlayer2();
        			}
        			else if(frin.getPlayer2().equals(p)){
        				pact=frin.getPlayer1();
        			}
        			if(pact.getPlaying()==null||(pact.getPlaying().equals(false))) {
        				LFC.add(frin);
        			}
        		}
        		Iterable<Friend> friendsNotPlaying=LFC;
        		modelMap.addAttribute("friendsNotPlaying", friendsNotPlaying);


        	}
        }
		return vista;
	}

	@GetMapping("/friendList/Pending")
	public String listFriendPending(ModelMap modelMap) {
		String vista = "friends/friendList";
		//Iterable<Friend>  friend = friendService.findAll();
		//modelMap.addAttribute("friends", friend);
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if(a!=null){
        	if(a.getPrincipal().toString() != "anonymousUser"){
        		Player p = playerService.getByUsername(a.getName());
        		Iterable<Friend>  friend = friendService.findFriendByReceiver(p);
        		modelMap.addAttribute("friendsPending", friend);

        	}

        }


		return vista;
	}


	@GetMapping("/friendList/invite/{friendId}")
	public String invitePlayer(@PathVariable("friendId") int friendId, ModelMap modelMap) {
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		Player p = playerService.getByUsername(a.getName());
		Friend relation = friendService.findFriendById(friendId).get();
		Player p1 = relation.getPlayer1();
		Player p2 = relation.getPlayer2();

		if(p.equals(p1)) {
			relation.setSender(p1);
			relation.setReceiver(p2);
		}
		else {
			relation.setSender(p2);
			relation.setReceiver(p1);
		}
		friendService.saveFriend(relation);
		return "redirect:/board/"+ p.getBoard().getId();
	}

	@GetMapping("friendList/deleteInvitation/{friendId}")
	public String deleteInvitation(@PathVariable("friendId") int friendId, ModelMap modelMap) {
		Optional<Friend> friend = friendService.findFriendById(friendId);
		Friend relation = friendService.findFriendById(friendId).get();
		relation.setSender(null);
		relation.setReceiver(null);
		friendService.saveFriend(relation);
		return "redirect:/friendList";
	}


	@GetMapping("/friendList/delete/{friendId}")
	public String deleteFriend(@PathVariable("friendId") int friendId, ModelMap modelMap) {
		String view = "friends/friendList";
		Optional<Friend> friend = friendService.findFriendById(friendId);
		if(friend.isPresent()) {
			friendService.delete(friend.get());
			modelMap.addAttribute("message", "Friend successfully deleted!");

		}else {
			modelMap.addAttribute("message", "Friend not found!");
		}
		return "redirect:/friendList";
	}


	@GetMapping("/friendList/details")
	public ModelAndView showFriends(ModelMap modelMap) {
		ModelAndView mav = new ModelAndView("friends/friendDetails");
		//mav.addObject(this.friendService.findFriendOfPlayer(username));

        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if(a!=null){
            if(a.getPrincipal().toString() != "anonymousUser"){


                    Player p = playerService.getByUsername(a.getName());
                 mav.addObject(this.friendService.findFriendOfPlayer(p));
                 return mav;
            }
            else {
            	return mav;
            }

        }
        else {
        	return mav;
        }

	}
	@GetMapping("/friendList/detailsPending")
	public ModelAndView showFriendsPending(ModelMap modelMap) {

		ModelAndView mav = new ModelAndView("friends/friendDetails");

        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if(a!=null){
            if(a.getPrincipal().toString() != "anonymousUser"){


                    Player p = playerService.getByUsername(a.getName());
                 mav.addObject(this.friendService.findFriendByReceiver(p));

                 return mav;

        }
            else {
            return mav;
            }
    }
        else {
        return mav;
        }
	}

	@GetMapping("/friendList/edit/{friendId}")
	public String processUpdateFriendForm(
			@PathVariable("friendId") int friendId) {
			FriendState FS=FriendState.ACCEPTED;
			Optional<Friend> f=friendService.findFriendById(friendId);
			if(f.isPresent()) {
                f.get().setFriendState(FS);
                this.friendService.saveFriend(f.get());
			}
			return "redirect:/friendList";
		}
	@GetMapping("friendList/new/{playerId}")
	public String createNewFriend(ModelMap modelMap, @PathVariable("playerId") int playerId) {
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		if(a!=null){
			if(a.getPrincipal().toString() != "anonymousUser"){
				Friend newFriend = new Friend();
				Friend newFriend2 = new Friend();
                Player p1 = playerService.getByUsername(a.getName());
                Player p2=playerService.findPlayerById(playerId).get();

                newFriend2.setPlayer1(p2);
                newFriend2.setPlayer2(p1);
                newFriend2.setFriendState(FriendState.PENDING);

                Optional<Friend> Of=null;
                Iterable<Friend> IF=friendService.findFriendByReceiver(p1);
                for(Friend fOIF:IF) {
                	if(fOIF.getPlayer1().equals(p2)) {

                		return "redirect:/friendList";


                	}

                }
                Iterable<Friend> IFF=friendService.findFriendOfPlayer(p1);
                for(Friend fOIF:IFF) {
                	if(fOIF.getPlayer1().equals(p2)||fOIF.getPlayer2().equals(p2)) {

                		return "redirect:/friendList";


                	}

                }


                modelMap.addAttribute("friend", newFriend);
                newFriend.setPlayer1(p1);
                newFriend.setPlayer2(p2);
                newFriend.setFriendState(FriendState.PENDING);


                friendService.saveFriend(newFriend);



				return "players/findPlayer" ;
			}else return "redirect:/login";
		}else return "redirect:/login";
	}




    @GetMapping("/sendRecomendationEmail")
    public String recomendationEmail(@PathParam("to") String to){
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(a);
        Player from = playerService.getByUsername(a.getName());
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject("Juega a Idus Martii");
        email.setText("Buenas, tu amigo/a " + from.getFirstName() + " " + from.getLastName() + " te invita a probar Idus Martii y jugar unas partidas, únete con este enlace haciendo click: http://localhost:8080/login");
        mailSender.send(email);
        return "redirect:/friendList";
    }

	}




