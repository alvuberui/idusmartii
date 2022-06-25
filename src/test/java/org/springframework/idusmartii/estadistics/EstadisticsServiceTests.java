package org.springframework.idusmartii.estadistics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class EstadisticsServiceTests {

	@Autowired
	protected EstadisticsService estadisticService;

	@Autowired
	protected PlayerService playerService;
	
	@Test
	@Transactional
	public void testSaveEstadistic() throws Exception{
		Estadistics testEstadistics = new Estadistics();
		testEstadistics.setRankingPos(200);

		try {
			this.estadisticService.save(testEstadistics);
		}catch (Exception ex){
			throw new Exception("fallos al guardar la estadistica");
		}
		
		
		assertEquals(testEstadistics,estadisticService.getEstadisticsById(6).get());
		
		
		assertEquals(6,estadisticService.findAll().spliterator().getExactSizeIfKnown());
		
	}

}
