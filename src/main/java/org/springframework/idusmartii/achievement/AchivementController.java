package org.springframework.idusmartii.achievement;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.idusmartii.estadistics.Estadistics;
import org.springframework.idusmartii.estadistics.EstadisticsService;
import org.springframework.idusmartii.players.Player;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AchivementController {

	private static final String VIEW_CREATE_OR_UPDATE_ACHIVEMENT = "achivement/createAchivement";

	@Autowired
	private AchivementService achivementsService;
	
	@Autowired
	private EstadisticsService estadisticService;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping("/achivement/list")
	public String achivementsList(ModelMap modelMap) {
		String vista = "/achivement/achivementPlayerList";
		Iterable<Achivement> achivements = achivementsService.findAll();
		modelMap.addAttribute("achivements",achivements);
		return vista;
	}
	
	@GetMapping("/admin/achivementsList")
	public String achivementsListAdmin(ModelMap modelMap) {
		String vista = "/achivement/achivementsList";
		Iterable<Achivement> achivements = achivementsService.findAll();
		modelMap.addAttribute("achivements",achivements);
		return vista;
	}
	
	@GetMapping("/admin/achivementsList/delete/{achivementId}")
	public String achivementsDeleteAdmin(@PathVariable("achivementId") int achivementId, ModelMap modelMap) {
		Optional<Achivement> achivement = achivementsService.findAchivementById(achivementId);
		String vista = "/admin/achivementsList";
		if(achivement.isPresent()) {
			achivementsService.delete(achivement.get());
		}
		
		return "redirect:" + vista;
	}
	
	@GetMapping("/admin/achivementsList/createachivement")
	public String createAchivement(Map<String, Object> model) {
		Achivement achivement = new Achivement();
		List<Condition> conditions = Arrays.asList(Condition.values());
		List<AplicableEntitys> aplicableEntity = Arrays.asList(AplicableEntitys.values());
		model.put("conditions", conditions);
		model.put("achivement", achivement);
		model.put("aplicableEntity", aplicableEntity);
        return "achivement/createAchivement";
	}
	
	@PostMapping(value = "/admin/achivementsList/createachivement")
	public String achivementCreationForm(@Valid Achivement achivement, BindingResult result,ModelMap model) {
		if (result.hasErrors()) {
			List<Condition> conditions = Arrays.asList(Condition.values());
			List<AplicableEntitys> aplicableEntity = Arrays.asList(AplicableEntitys.values());
			model.put("aplicableEntity", aplicableEntity);
			model.put("conditions", conditions);
			return VIEW_CREATE_OR_UPDATE_ACHIVEMENT; 
		} else {
			this.achivementsService.create(achivement);
			return "redirect:/admin/achivementsList";
		}
	}
	
	@GetMapping("/admin/achivementsList/update/{achivementId}")
	public String updateAchivement(Map<String, Object> model, @PathVariable("achivementId") int achivementId, ModelMap modelMap) {
		Achivement achivement = new Achivement();
		List<Condition> conditions = Arrays.asList(Condition.values());
		model.put("conditions", conditions);
		List<AplicableEntitys> aplicableEntity = Arrays.asList(AplicableEntitys.values());
		model.put("aplicableEntity", aplicableEntity);
		model.put("achivement", achivement);
		List<Estadistics > estadistics = (List<Estadistics>) estadisticService.findAll();
		
		model.put("estadistics", estadistics);
		Achivement oldAchivement = achivementsService.findAchivementById(achivementId).get();
		modelMap.put("oldachivement", oldAchivement);
        return "achivement/updateAchivement";
	}
	
	@PostMapping(value = "/admin/achivementsList/update/{achivementId}")
	public String processUpdatingForm(@Valid Achivement achivement, BindingResult result, @PathVariable("achivementId") int achivementId) {
		if (result.hasErrors()) {
			return "achivement/updateAchivement";
		}
		else {
			achivement.setId(achivementId);
			this.achivementsService.create(achivement);

			return "redirect:/admin/achivementsList";
		}
	}


}
