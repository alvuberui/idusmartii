package org.springframework.idusmartii.achivement;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.idusmartii.achievement.Achivement;
import org.springframework.idusmartii.achievement.AchivementController;
import org.springframework.idusmartii.achievement.AchivementService;
import org.springframework.idusmartii.achievement.AplicableEntitys;
import org.springframework.idusmartii.achievement.Condition;
import org.springframework.idusmartii.configuration.SecurityConfiguration;
import org.springframework.idusmartii.estadistics.Estadistics;
import org.springframework.idusmartii.estadistics.EstadisticsService;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AchivementController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

public class AchivementControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
    private AchivementService achivementService;
	
	@MockBean
    private EstadisticsService estadisticsService;
	
	@MockBean
    private AuthoritiesService authoritiesService;
	
	@MockBean
    private PlayerService playerService;
	
	@BeforeEach
    void setup(){
        Achivement testAchivement = new Achivement();
        testAchivement.setId(200);
        testAchivement.setTitle("Prueba de Achivement");
        testAchivement.setDescription("Prueba de descripción");
        testAchivement.setConditions(Condition.EQUAL_OR_MORE_THAN);
        testAchivement.setQuantity(10);
        testAchivement.setAplicableEntity(AplicableEntitys.MATCH_WINS);
    }
	
	@WithMockUser(value = "spring")
    @Test
    void testAchivementList() throws Exception{
		Iterable<Achivement> achivementList = achivementService.findAll();
		given(achivementList).willReturn(achivementList);
    	mockMvc.perform(get("/achivement/list"))
    		.andExpect(model().attributeExists("achivements"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("/achivement/achivementPlayerList"));
    		
    }
	
	
	@WithMockUser(value = "spring")
    @Test
    void testAchivementListAdmin() throws Exception{
		Iterable<Achivement> achivementList = achivementService.findAll();
		given(achivementList).willReturn(achivementList);
    	mockMvc.perform(get("/admin/achivementsList"))
    		.andExpect(model().attributeExists("achivements"))
    		.andExpect(status().isOk())
    		.andExpect(view().name("/achivement/achivementsList"));
    		
    }
	
	
	@WithMockUser(value = "spring")
    @Test
    void testDeleteAchivementAdminSuccesful() throws Exception{
		Optional<Achivement> newAchivement = Optional.of(new Achivement());
		given(achivementService.findAchivementById(200)).willReturn(newAchivement);
		mockMvc.perform(get("/admin/achivementsList/delete/{achivementId}",200))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/admin/achivementsList"));
	}
	
	
	@WithMockUser(value = "spring")
    @Test
    void testDeleteAchivementAdminFail() throws Exception{
		mockMvc.perform(get("/admin/achivementsList/delete/{achivementId}",100000))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/admin/achivementsList"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testGetCreateeAchivementAdminSuccesful() throws Exception{
		mockMvc.perform(get("/admin/achivementsList/createachivement"))
		.andExpect(model().attributeExists("conditions"))
		.andExpect(model().attributeExists("achivement"))
		.andExpect(model().attributeExists("aplicableEntity"))
		.andExpect(view().name("achivement/createAchivement"));
	}
	
	
	@WithMockUser(value = "spring")
    @Test
	void testPostCreateeAchivementAdminSuccesful() throws Exception{
		 mockMvc.perform(post("/admin/achivementsList/createachivement").with(csrf())
		            .param("title", "Prueba de Achivement1")
		            .param("description", "Prueba de descripción 1")
		            .param("conditions",Condition.EQUAL_OR_MORE_THAN.toString())
		            .param("quantity","5")
		            .param("aplicableEntity",AplicableEntitys.MATCH_WINS.toString())
		            ).andExpect(view().name("redirect:/admin/achivementsList"));	
	}
	
	
	@WithMockUser(value = "spring")
    @Test
	void testPostCreateeAchivementAdminFail() throws Exception{
		 mockMvc.perform(post("/admin/achivementsList/createachivement").with(csrf())
		            .param("title", "Prueba de Achivement1")
		            .param("description", "Prueba de descripción 1")
		            .param("conditions","")
		            .param("quantity","5")
		            .param("aplicableEntity",""))
		            .andExpect(view().name("achivement/createAchivement"));	
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testUpdateAchivementGetSuccesfull() throws Exception{
		Optional<Achivement> newAchivement = Optional.of(new Achivement());
		given(achivementService.findAchivementById(200)).willReturn(newAchivement);
		mockMvc.perform(get("/admin/achivementsList/update/{achivementId}",200))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("conditions"))
		.andExpect(model().attributeExists("aplicableEntity"))
		.andExpect(model().attributeExists("achivement"))
		.andExpect(model().attributeExists("estadistics"))
		.andExpect(model().attributeExists("oldachivement"))
		.andExpect(view().name("achivement/updateAchivement"));	
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testUpdateAchivementPostSuccesfull() throws Exception{
		mockMvc.perform(post("/admin/achivementsList/update/{achivementId}",200).with(csrf())
				.param("title", "Prueba de Achivement 1")
	            .param("description", "Prueba de descripción 1")
	            .param("conditions",Condition.EQUAL_OR_MORE_THAN.toString())
	            .param("quantity","5")
	            .param("aplicableEntity",AplicableEntitys.MATCH_WINS.toString())
	            ).andExpect(view().name("redirect:/admin/achivementsList"));	
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testUpdateAchivementPostFail() throws Exception{
		mockMvc.perform(post("/admin/achivementsList/update/{achivementId}",200).with(csrf())
				.param("title", "Prueba de Achivement1")
	            .param("description", "Prueba de descripción 1")
	            .param("conditions",Condition.EQUAL_OR_MORE_THAN.toString())
	            .param("quantity","5")
	            .param("aplicableEntity","0")
	            ).andExpect(view().name("achivement/updateAchivement"));	
	}
	
	

}
