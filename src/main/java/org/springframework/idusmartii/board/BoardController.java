package org.springframework.idusmartii.board;


import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.match.MatchRepository;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.friends.Friend;
import org.springframework.idusmartii.friends.FriendService;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.turn.Turn;
import org.springframework.idusmartii.user.Authorities;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class BoardController {


	@Autowired
	private BoardService boardService;



	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private FriendService friendService;


    /*
     * alvuberui: devuelve los jugadores sentado en una mesa,
     * dada la id de la mesa
     */
    @GetMapping("/board/{boardId}") //url
    public String listPlayerByBoardId(@PathVariable("boardId") int boardId, HttpServletResponse response, Map<String, Object> modelMap) {
    	response.addHeader("Refresh", "2");
    	Authentication a = SecurityContextHolder.getContext().getAuthentication();
    	Player p = playerService.getByUsername(a.getName());
		Iterable<Friend>  friend = friendService.findFriendOfPlayer(p);
		modelMap.put("friends", friend);
		String vista = "boards/board";
    	
    	Board b = this.boardService.findById(boardId).get();
	    Match m = b.getPlayingMatch();
	    modelMap.put("board", b);
	    modelMap.put("players", this.playerService.findPlayerByBoardId(boardId));
	    modelMap.put("player", SecurityContextHolder.getContext().getAuthentication().getName());
        if( m!= null){
            return "redirect:/match/" + m.getId();
        }
        modelMap.put("board", b);
        modelMap.put("players", this.playerService.findPlayerByBoardId(boardId));
        modelMap.put("player", SecurityContextHolder.getContext().getAuthentication().getName());
        return vista;
    }

    @GetMapping("/board/watch/{boardId}") //url
    public String watchBoardId(@PathVariable("boardId") int boardId, HttpServletResponse response, ModelMap modelMap) {
        response.addHeader("Refresh", "2");
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        Board b = this.boardService.findById(boardId).get();
        Match m = b.getPlayingMatch();
        if(a!=null && a.getPrincipal().toString() != "anonymousUser"){
            Player player = playerService.getByUsername(a.getName());
            if(m.getPlayerList().contains(player)){
                return "redirect:/match/" + m.getId();
            }
        }else{
            return "redirect:/login";
        }
        modelMap.addAttribute("board", b);
        modelMap.addAttribute("players", this.playerService.findPlayerByBoardId(boardId));
        modelMap.addAttribute("player", SecurityContextHolder.getContext().getAuthentication().getName());
        if(m.getTurnList() != null && m.getTurnList().size() > 0){
            Turn lastTurn = m.getTurnList().stream().sorted(Comparator.comparing(Turn::getTurn)).collect(Collectors.toList()).get(m.getTurnList().size()-1);
            modelMap.addAttribute("turn", lastTurn);
            modelMap.addAttribute("turnLog", lastTurn.toString2());
        }
        modelMap.addAttribute("watching", true);
        return "boards/board";
    }


	@GetMapping("play")
	public String boards(ModelMap modelMap, HttpServletResponse response) {
        response.addHeader("Refresh", "3");
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		if(a!=null){
			if(a.getPrincipal().toString() != "anonymousUser"){
                if(playerService.getByUsername(a.getName()) != null){
                    Player p = playerService.getByUsername(a.getName());
                    if(p.getBoard()!= null){
                        Board board = p.getBoard();
                        return "redirect:board/" + board.getId();
                    }
                }
                    String vista = "/play/boardsList";
                    Iterable<Board> boards = boardService.findAll();
                    for (Board board : boards) {
                        board.setNumPlayers(playerService.findPlayerByBoardId(board.getId()).size());
                    }
                    modelMap.addAttribute("boards", boards);
                    return vista;

			}
		}
        return "redirect:/login";
	}


	@GetMapping("play/new")
	public String createNewBoard(ModelMap modelMap) {
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		if(a!=null){
			if(a.getPrincipal().toString() != "anonymousUser"){
				Board newBoard = new Board();
                Player p = playerService.getByUsername(a.getName());
				modelMap.addAttribute("board", newBoard);
                newBoard.setAnfitrion(p);
                boardService.saveBoard(newBoard);
                p.setPlaying(true);
				p.setBoard(newBoard);
				playerService.savePlayer(p);
				return "redirect:/board/" + newBoard.getId();
			}else return "redirect:/login";
		}else return "redirect:/login";
	}



    @GetMapping("play/join/{boardId}") //Testeado 1 Repasar
    public String boardJoin(@PathVariable("boardId") int boardId, ModelMap modelMap) {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if(a!=null){
            if(a.getPrincipal().toString() != "anonymousUser"){
                Board board = boardService.findById(boardId).get();
                if(board!=null){
                    Long numPlayer = playerService.findPlayerByBoardId(boardId).stream().count();
                    if(numPlayer < 8){
                        String vista = "/board/"+ boardId;
                        Player p = playerService.getByUsername(a.getName());
                        p.setBoard(board);
                        p.setPlaying(true);
                        playerService.savePlayer(p);
                        return "redirect:" + vista;
                    }else{
                        return "redirect:/play";
                    }
                }
            }else {
                return "redirect:/login";
            }
        }
        return "redirect:/login";
    }
    
    @GetMapping("play/join/{boardId}/{friendId}") 
    public String boardJoinWithInvitation(@PathVariable("boardId") int boardId,@PathVariable("friendId") int friendId, ModelMap modelMap) {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        Friend relation = friendService.findFriendById(friendId).get();
        if(a!=null){
            if(a.getPrincipal().toString() != "anonymousUser"){
                Board board = boardService.findById(boardId).get();
                if(board!=null){
                    Long numPlayer = playerService.findPlayerByBoardId(boardId).stream().count();
                    if(numPlayer < 8){
                    	relation.setSender(null);
                    	relation.setReceiver(null);
                    	friendService.saveFriend(relation);
                        String vista = "/board/"+ boardId;
                        Player p = playerService.getByUsername(a.getName());
                        p.setBoard(board);
                        p.setPlaying(true);
                        playerService.savePlayer(p);
                        return "redirect:" + vista;
                    }else{
                        return "redirect:/play";
                    }
                }
            }else {
                return "redirect:/login";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("play/left")
    public String boardLeft() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if(a!=null){
            if(a.getPrincipal().toString() != "anonymousUser"){
                Player p = playerService.getByUsername(a.getName());
                
                Board board = p.getBoard();
                if(board!=null){
                    Collection<Player> players = playerService.findPlayerByBoardId(board.getId());
                        p.setBoard(null);
                        p.setPlaying(false);
                        playerService.savePlayer(p);
                        if(players.size()==1){
                            boardService.deleteBoard(board.getId());
                        }else if(board.getAnfitrion() != null && board.getAnfitrion().getId()==p.getId()){
                            board.setAnfitrion(players.stream().filter(x-> x.getUser().getUsername().toString() != a.getName().toString()).collect(Collectors.toList()).get(0));
                            boardService.saveBoard(board);
                        }
                    return "redirect:/play";
                }
            }else {
                return "redirect:/login";
            }
        }
        return "redirect:/login";
    }


}
