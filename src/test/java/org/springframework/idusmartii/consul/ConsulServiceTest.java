package org.springframework.idusmartii.consul;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.idusmartii.board.Board;
import org.springframework.idusmartii.board.BoardService;

import org.springframework.idusmartii.faction.Faction;
import org.springframework.idusmartii.faction.FactionCard;
import org.springframework.idusmartii.faction.FactionService;
import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.match.MatchService;
import org.springframework.idusmartii.match.MatchTurnStatus;
import org.springframework.idusmartii.players.Player;
import org.springframework.idusmartii.players.PlayerService;
import org.springframework.idusmartii.turn.Turn;
import org.springframework.idusmartii.turn.TurnService;
import org.springframework.idusmartii.user.AuthoritiesService;
import org.springframework.idusmartii.user.User;
import org.springframework.idusmartii.user.UserService;
import org.springframework.idusmartii.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ConsulServiceTest {

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

	@Autowired
	private FactionService factionService;


    @Test
    void shouldFindConsulWithCorrectId(){
        Optional<Consul> consulOptional= consulService.findConsulById(1);
        consulOptional.ifPresent(consul -> {
            assertThat(consul.getPlayer().getId()).isEqualTo(10);
            assertThat(consul.getFaction().getCardType()).isEqualTo(Faction.MERCHANT);
        });
    }

    @Test
    void shouldCountAllConsuls(){
        Integer numConsuls = consulService.consulCount();
        assertThat(numConsuls).isEqualTo(1);
    }

    @Test
    void shouldFindAllConuls(){
        Iterable<Consul> consuls = consulService.findAll();
        Consul consul1 = EntityUtils.getById((Collection<Consul>)consuls, Consul.class, 1);
        assertThat(consul1.getPlayer().getId()).isEqualTo(10);
        assertThat(consul1.getFaction().getCardType()).isEqualTo(Faction.MERCHANT);
    }

    @Test
    void shouldInsertConsulIntoDatabaseAndGenerateId(){
        int numConsulBefore = consulService.consulCount();
        Optional<Player> newPlayer = playerService.findPlayerById(9);
        Consul newConsul = new Consul(newPlayer.get());
        consulService.save(newConsul);
        int numConsulAfter = consulService.consulCount();
        assertThat(numConsulAfter).isEqualTo(numConsulBefore + 1);
        assertThat(newConsul.getId()).isNotNull();
    }

    @Test
    void shouldDeleteConsulFromDatabase(){
        Optional<Player> newPlayer = playerService.findPlayerById(9);
        Consul newConsul = new Consul(newPlayer.get());
        consulService.save(newConsul);
        int numConsulBefore = consulService.consulCount(); // 2
        consulService.delete(newConsul);
        int numConsulAfter = consulService.consulCount(); // 1
        assertThat(numConsulAfter + 1).isEqualTo(numConsulBefore);

    }

    @Test
    void shouldFoundConsulWithPlayerAndMatch(){

        Match newMatch = new Match(5);
        Board newBoard = new Board(5);
        boardService.saveBoard(newBoard);
        newMatch.setBoard(newBoard);
        Turn newTurn = new Turn(newMatch, 1);
        matchService.save(newMatch);
        newMatch.setTurnList( Lists.newArrayList(newTurn));
        Optional<Consul> consulOptional= consulService.findConsulById(1);
        newTurn.setConsul(consulOptional.get());
        turnService.save(newTurn);
        Optional<Player> player = playerService.findPlayerById(10);

        Optional<Consul> consulTest = consulService.getConsulByPlayer(player.get(), newMatch);

        assertThat(consulTest.get()).isEqualTo(consulOptional.get());

    }

}
