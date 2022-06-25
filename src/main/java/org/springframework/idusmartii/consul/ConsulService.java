package org.springframework.idusmartii.consul;

import java.util.Optional;

import org.springframework.idusmartii.match.Match;
import org.springframework.idusmartii.players.Player;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ConsulService {

    @Autowired
    private ConsulRepository consulRepo;

    @Transactional
	public int consulCount() { //Testeado
		return (int) consulRepo.count();
	} //TESTEADO

    @Transactional
	public Iterable<Consul> findAll() { //Testeado
		return consulRepo.findAll();
	} //TESTEADO

    @Transactional
	public void save(Consul consul) { //TESTEADO
		consulRepo.save(consul);

	}

    public void delete(Consul consul) { //Testeado
		consulRepo.delete(consul);

	}
    @Transactional(readOnly=true)
	public Optional<Consul> findConsulById(int consulId) { //Testeado
		return consulRepo.findById(consulId);
	} //TESTEADO

    @Transactional(readOnly=true)
    public Optional<Consul> getConsulByPlayer(Player player, Match match) {
        return consulRepo.getConsulByPlayer(match, player);
    }
}
