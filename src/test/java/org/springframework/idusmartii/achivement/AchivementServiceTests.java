package org.springframework.idusmartii.achivement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.idusmartii.achievement.Achivement;
import org.springframework.idusmartii.achievement.AchivementService;
import org.springframework.idusmartii.achievement.AplicableEntitys;
import org.springframework.idusmartii.achievement.Condition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AchivementServiceTests {
	
	@Autowired
	protected AchivementService achivementService;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@ParameterizedTest
	@CsvSource({
	"Prueba1, prueba de descripcion,EQUAL_OR_MORE_THAN,5,MATCH_WINS", //Esta sera la primera ya que es la que furula bien
	"null, prueba de descripcion,EQUAL_OR_MORE_THAN,5,MATCH_WINS", //title null
	"Prueba3, null,EQUAL_OR_MORE_THAN,5,MATCH_WINS", //descripcion null
	"P7, prueba de descripcion,EQUAL_OR_MORE_THAN,5,MATCH_WINS", //Titulo menos de 5 letras
	"P8-1234567890123456789012345, prueba de descripcion,EQUAL_OR_MORE_THAN,5,MATCH_WINS", //Titulo mas de 20 letras
	})
	@Transactional
	public void voidTestWithCSVPatametizedSaveAchivement(String title,String description,Condition condition,Integer quantity, AplicableEntitys aplicableEntity) throws Exception {
		Achivement testAchivement = new Achivement();
		
		testAchivement.setTitle(title);
		testAchivement.setDescription(description);
		testAchivement.setConditions(condition);
		testAchivement.setQuantity(quantity);
		
		testAchivement.setAplicableEntity(aplicableEntity);
		
		try {
			this.achivementService.create(testAchivement);
		}catch (Exception ex){
			System.out.println("fallos al guardar el achivement " + testAchivement.getTitle());
		}
		
		
	}
	
	
	

}
