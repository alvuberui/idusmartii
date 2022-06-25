package org.springframework.idusmartii.pretor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.idusmartii.board.BoardService;
import org.springframework.idusmartii.consul.ConsulService;
import org.springframework.idusmartii.edil.EdilService;
import org.springframework.idusmartii.match.MatchService;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.turn.TurnService;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.UserService;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PretorServiceTest {


	@Autowired
	private PretorService pretorService;

	@Autowired
	private EdilService edilService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	private UserService userService;

	@Autowired
	private ConsulService consulService;

	@Autowired
	private TurnService turnService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private BoardService boardService;



    @Test
    void shouldFindAll(){
        int num = pretorService.pretorCount();
        List<Pretor> allpretor = (List<Pretor>)pretorService.findAll();
        assertThat(allpretor.size()).isEqualTo(num);
    }

    @Test
    void shouldFindPretorById(){
        Pretor newPretor = new Pretor(playerService.findPlayerById(10).get());
        pretorService.save(newPretor);
        Optional<Pretor> pretor = pretorService.findPretorById(newPretor.getId());
        pretor.ifPresent(value -> assertThat(value.getId()).isEqualTo(newPretor.getId()));
        pretorService.delete(newPretor);
    }

    @Test
    void shouldNotFindPretorById(){
        Pretor newPretor = new Pretor(playerService.findPlayerById(10).get());
        pretorService.save(newPretor);
        Optional<Pretor> pretor = pretorService.findPretorById(1000);
        assertThat(pretor).isEmpty();
    }

    @Test
    void  shouldSave(){
        int numBefore = pretorService.pretorCount();
        Pretor newPretor = new Pretor(playerService.findPlayerById(10).get());
        pretorService.save(newPretor);
        int numAfter = pretorService.pretorCount();
        assertThat(numBefore +1).isEqualTo(numAfter);
    }

    @Test
    void  shouldNotSave(){
        Pretor newPretor = new Pretor();
        Assertions.assertThrows(javax.validation.ConstraintViolationException.class, () ->{
            this.pretorService.save(newPretor);});
    }







}
