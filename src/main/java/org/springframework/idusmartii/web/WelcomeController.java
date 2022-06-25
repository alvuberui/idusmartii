package org.springframework.idusmartii.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.board.Board;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

	@Autowired
	private PlayerService playerService;

	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(a);
		if(a!=null){
			System.out.println();
			if(a.getPrincipal().toString() != "anonymousUser"){
                if(playerService.getByUsername(a.getName()) != null){
                    Player p = playerService.getByUsername(a.getName());
                    if(p.getBoard()!= null){
                        Board board = p.getBoard();
                        return "redirect:board/" + board.getId();
                    }
				}
			}
		} return "welcome";
	  }
}
