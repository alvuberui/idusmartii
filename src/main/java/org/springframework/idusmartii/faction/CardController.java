/* package org.springframework.samples.petclinic.faction;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.players.Player;
import org.springframework.samples.petclinic.players.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CardController {

	@Autowired
<<<<<<< HEAD:src/main/java/org/springframework/samples/petclinic/faction/CardController.java
	private FactionService cardService;
=======
	private CardService cardService;
>>>>>>> master:src/main/java/org/springframework/samples/petclinic/card/CardController.java

	@GetMapping("/cardList")
	public String listCard(ModelMap modelMap) {
		String vista = "cards/cardList";
		Iterable<Card>  cards = cardService.findAll();
		modelMap.addAttribute("cards", cards);
		return vista;

	}

	@GetMapping("/cardList/new")
	public String createCard(ModelMap modelMap) {
		String view = "cards/createOrUpdateCardForm";
		modelMap.addAttribute("card", new Card());
		return view;

	}

	@PostMapping("/cardList/new")
	public String saveCard(@Valid Card card, BindingResult result, ModelMap modelMap) {
		String view = "cards/cardList";

		if(result.hasErrors()) {
			modelMap.addAttribute("card", card);
			return "redirect:/cardList/new";
		}
		cardService.save(card);
		modelMap.addAttribute("message", "Card successfully saved!");
		return "redirect:/cardList";
	}


	@GetMapping("/cardList/delete/{cardId}")
	public String deleteCard(@PathVariable("cardId") int cardId, ModelMap modelMap) {
		String view = "cards/cardList";
		Optional<Card> card = cardService.findCardById(cardId);
		if(card.isPresent()) {
			cardService.delete(card.get());
			modelMap.addAttribute("message", "Card successfully deleted!");

		}else modelMap.addAttribute("message", "Card not found!");
		return "redirect:/cardList";
	}


	@GetMapping("/cardList/{cardId}")
	public ModelAndView showCard(@PathVariable("cardId") int cardId) {
		ModelAndView mav = new ModelAndView("cards/cardDetails");
		mav.addObject(this.cardService.findCardById(cardId).get());
		return mav;

	}
	@GetMapping("/cardList/{cardId}/edit")
	public String initUpdateCardForm(@PathVariable("cardId") int cardId, Model model) {
		String view = "cards/createOrUpdateCardForm";
		Card cards = this.cardService.findCardById(cardId).get();
		model.addAttribute(cards);
		System.out.println(model.addAttribute(cards));
		return view;
	}

	@PostMapping("/cardList/{cardId}/edit")
	public String processUpdateCardForm(@Valid Card card, BindingResult result,
			@PathVariable("cardId") int cardId) {
		if (result.hasErrors()) {
			return  "cards/createOrUpdateCardForm";
		}
		else {
			card.setId(cardId);
			this.cardService.save(card);
			return "redirect:/cardList/{cardId}";
		}
	}
}
 */