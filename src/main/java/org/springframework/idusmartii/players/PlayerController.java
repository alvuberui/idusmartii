package org.springframework.idusmartii.players;



import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.idusmartii.achievement.Achivement;
import org.springframework.idusmartii.achievement.AchivementService;
import org.springframework.idusmartii.achievement.AplicableEntitys;
import org.springframework.idusmartii.achievement.Condition;
import org.springframework.idusmartii.board.Board;
import org.springframework.idusmartii.estadistics.Estadistics;
import org.springframework.idusmartii.estadistics.EstadisticsService;
import org.springframework.idusmartii.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class PlayerController {

	private static final String VIEW_CREATE_PLAYER = "players/createPlayer";

	@Autowired
	private PlayerService playerService;

	@Autowired
	private UserService userService;

    @Autowired
    private EstadisticsService estadisticsService;

    @Autowired
    private AchivementService achivementService;


    //TESTEADO
	@GetMapping("/admin/playersList")
	public String listPlayer(ModelMap modelMap, Pageable pageable) {
		String vista = "players/playerList";
        pageable = PageRequest.of(pageable.getPageNumber(),3);
        Page<Player> pages = playerService.getPage(pageable);
        modelMap.addAttribute("number", pages.getNumber());
        modelMap.addAttribute("totalPages", pages.getTotalPages());
        modelMap.addAttribute("totalElements", pages.getTotalElements());
        modelMap.addAttribute("size", pages.getSize());
        modelMap.addAttribute("data",pages.getContent());
        return vista;
	}


	@GetMapping("/admin/playersList/createPlayer") //TESTEADo
	public String createPlayerV(Map<String, Object> model, ModelMap modelMap) {
		Player player = new Player();
		model.put("player", player);
        return "players/createPlayer";
	}

	/*
	 * alvuberui: controlador para el formulario de administrador
	 * para crear datos de un jugador
	 */
	@PostMapping(value = "/admin/playersList/createPlayer") //TESTEADo
	public String processCreationForm(@Valid Player player, BindingResult result) {
		if (result.hasErrors()) {
			return VIEW_CREATE_PLAYER;
		}
		else {
			//creating owner, user and authorities
			Estadistics estadistic = new Estadistics();
			this.estadisticsService.save(estadistic);
			player.setEstadistic(estadistic);
			this.playerService.savePlayer(player);

			return "redirect:/admin/playersList";
		}
	}


	@GetMapping("/admin/playersList/update/{playerId}") //TESTEADo
	public String updatePlayerV(Map<String, Object> model, @PathVariable("playerId") int playerId, ModelMap modelMap) {
		Player player = new Player();
		model.put("player", player);
		Optional<Player> oldPlayer = playerService.findPlayerById(playerId);
        oldPlayer.ifPresent(value -> modelMap.put("oldplayer", value));
        if(oldPlayer.isEmpty()) model.put("message", "No existe jugador");
        return "players/updatePlayer";
	}

	/*
	 * alvuberui: controlador para el formulario de administrador
	 * para actualizar datos de un jugador
	 */
	
	@PostMapping(value = "/admin/playersList/update/{playerId}") //TESTEADO
	public String proccessUpdatingForm(@Valid Player player, BindingResult result, @PathVariable("playerId") int playerId) {
		if (result.hasErrors()) {
			return VIEW_CREATE_PLAYER;
		}
		else {
			Player playerForEstadistics = playerService.findPlayerById(playerId).get();
			Estadistics playerEstadistics = playerForEstadistics.getEstadistic();
			player.setEstadistic(playerEstadistics);
			player.setId(playerId);
			if(!playerService.findPlayerById(playerId).get().getUser().getUsername().equals(player.getUser().getUsername())) {
	            if(this.playerService.getByUsername(player.getUser().getUsername()) != null) {
	                result.rejectValue("user.username", "", "Este nombre de usuario ya esta en uso");
	                return VIEW_CREATE_PLAYER;
	            }else{
	            	userService.deleteUser(playerService.findPlayerById(playerId).get().getUser().getUsername());
	                this.playerService.savePlayer(player);
	                return "redirect:/admin/playersList";
	            }
	        }
			
			return "redirect:/admin/playersList";
		}
		
	}



	/*
	 * alvuberui: devuelve los jugadores jugando una partida,
	 * dada la id de la partida
	 */

	@GetMapping("/playerList/match/{matchId}") //url Funciona??
	public ModelAndView listPlayerByMatchId(@PathVariable("matchId") int matchId) {
		ModelAndView mav = new ModelAndView("matchs/match"); //vista
		mav.addObject("players", this.playerService.findPlayerByMatchId(matchId));
		return mav;
	}

	/*
	 * alvuberui: elimina a un jugador a teravés de su id (solo administrador)
	 */
	
	@GetMapping("/admin/playerList/delete/{playerId}") //Testeado
	public String deletePlayer(@PathVariable("playerId") int playerId, ModelMap modelMap) {
		Optional<Player> player = playerService.findPlayerById(playerId);
		if(player.isPresent()) {
			playerService.delete(player.get());
			modelMap.addAttribute("message", "Player successfully deleted!");
		}
		else modelMap.addAttribute("message", "Player not found!");
		return "redirect:/admin/playersList";
	}

	/*
	 * Permite ver los datos del propio usuario : juagomram4
	 * Falla si la condicion de cantidad es 1 (a revisar) o si se le dan parametros nulos, por lo tanto ahora mismo fallara hasta que
	 * el logro que esta en base de datos se arregle a como deberia
	 *
	 * Hace falta cambiar el JSP para que se vea mas bonito, por el resto esta todo listo
	 */
	@GetMapping("/details/{userName}")
	public ModelAndView showPlayer(@PathVariable("userName") String userName) {
		ModelAndView mav = new ModelAndView("players/playerDetails");
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		if(a!=null){
			if(a.getPrincipal().toString() != "anonymousUser" && a.getName().equals(userName)){
				Player actualPlayer = playerService.getByUsername(userName);
				achivementIncreaser(actualPlayer);
				Estadistics playerEstadistics = actualPlayer.getEstadistic();
				mav.addObject("achivements",playerEstadistics.getAchivementList());
				mav.addObject("player",actualPlayer);
				mav.addObject("estadistic", playerEstadistics);
				String longuer = IntegerAMinSegs(playerEstadistics.getMatchLongerDuration());
				String shorter = IntegerAMinSegs(playerEstadistics.getMatchShorterDuration());
				mav.addObject("shorter",shorter);
				mav.addObject("longer", longuer);
			}
			return mav;
		} else {
			return new ModelAndView("/error");
		}

	}


	@GetMapping("/player/edit")
    public ModelAndView getUpdatePlayerForm() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        Player player = this.playerService.getByUsername(a.getName());
        ModelAndView mav = new ModelAndView("users/createPlayerForm");
        mav.addObject("player", player);
        return mav;
    }
	
    @PostMapping("/player/edit")
    public String processUpdatePlayerForm(@Valid Player newPlayer, BindingResult result) {
        if (result.hasErrors()) {
            return  "users/createPlayerForm";
        } else {
            Authentication a = SecurityContextHolder.getContext().getAuthentication();
            Player player = this.playerService.getByUsername(a.getName());
            Estadistics playerEstadistics = player.getEstadistic();
            newPlayer.setId(player.getId());
            newPlayer.setEstadistic(playerEstadistics);
            if(!a.getName().equals(newPlayer.getUser().getUsername())) {
                if(this.playerService.getByUsername(newPlayer.getUser().getUsername()) != null) {
                    result.rejectValue("user.username", "", "Este nombre de usuario ya esta en uso");
                    return "/users/createPlayerForm";
                }else{
                    this.playerService.savePlayer(newPlayer);
                    userService.deleteUser(a.getName());
                    return "redirect:/login";
                }
            }
            return "redirect:/playerList/details/" + player.getId();
        }
    }



    @GetMapping("/findPlayer")
    public String findPlayers(ModelMap modelMap, @PathParam("username") String username){
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if(a!=null){
            if(a.getPrincipal().toString() != "anonymousUser"){
                System.out.println(a.getName());
                Player p = playerService.getByUsername(a.getName());
                System.out.println(p);
                if(p != null && p.getBoard()!= null){
                    Board board = p.getBoard();
                    return "redirect:board/" + board.getId();
                }else{
                    String vista = "players/findPlayer";
                    System.out.println(username);
                    if(username.length() == 0){
                        modelMap.addAttribute("message", "Debe tener al menos un caracter para realizar la busqueda");
                        return vista;
                    }
                    List<Player> players = playerService.findByUsernameContaining(username);
                    System.out.println(players);
                    if (players.size() == 0){
                        modelMap.addAttribute("message", "No hay ningun jugador con ese nombre");
                        return vista;
                    }else{
                        modelMap.addAttribute("players", players);
                        modelMap.addAttribute("user", p);
                    return vista;
                    }
                }
            }
        }return "redirect:/login";
    }



    /*
     *
     * Metodos para revisar si los logros estan ya donde deberian
     *
     */

    private void achivementIncreaser(Player player) {
		Estadistics estadistic = player.getEstadistic();
		List<Achivement> achivementPlayerList = estadistic.getAchivementList();
		List<Achivement> allAchivement = IteratorToStream.iterableToStream(achivementService.findAll()).filter(x->!achivementPlayerList.contains(x)).collect(Collectors.toList());
		for(int i = 0; i<allAchivement.size();i++) {
			Achivement actualAchivement = allAchivement.get(i);
			if(achivementComparator(actualAchivement,estadistic)) {
				player.getEstadistic().getAchivementList().add(actualAchivement);
				estadisticsService.save(player.getEstadistic());

			}

		}

	}


	private boolean achivementComparator(Achivement achivementToRegulate,Estadistics playerEstadistic) {
		boolean res = false;
		AplicableEntitys entity = achivementToRegulate.getAplicableEntity();
		Integer quantity = achivementToRegulate.getQuantity();
		Condition condition = achivementToRegulate.getConditions();
		if(condition.toString().equals(Condition.EQUAL_OR_LESS_THAN.toString())) {
			res = quantity >= entityGetter(entity,playerEstadistic);
		} else {
			res = quantity <= entityGetter(entity, playerEstadistic);
		}
		return res;
	}

	private Integer entityGetter(AplicableEntitys entity,Estadistics playerEstadistic) {
		Integer res = 0;
		switch (entity) {
		case MATCH_WINS :
				res = playerEstadistic.getMatchsWins();
		case POINTS:
				res = playerEstadistic.getPoints();
		case RANKING:
				res = playerEstadistic.getRankingPos();
		case MATCHS_PLAYED:
				res = playerEstadistic.getMatchsPlayed();
		case WIN_STRIKE:
				res = playerEstadistic.getWinsLongerStrike();
		}
		return res;
	}

    /*
     *
     * final metodos logros
     *
     */

    private String IntegerAMinSegs(Integer matchLongerDuration) {
		int minutos = (int)Math.floor(matchLongerDuration/60);
		int segundos = (matchLongerDuration)-(minutos*60);
		String res = minutos + " minutos y " + segundos + " segundos.";
		return res;
	}


}
