package org.springframework.idusmartii.pretor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PretorService {

    @Autowired
    private PretorRepository pretorRepo;

    @Transactional
    public int pretorCount() { //Testeado
        return (int) pretorRepo.count();
    }

    @Transactional
    public Iterable<Pretor> findAll() { //Testedo
        return pretorRepo.findAll();
    }

    @Transactional(readOnly=true) //Testeado
    public Optional<Pretor> findPretorById(int pretorId) {
        return pretorRepo.findPretorById(pretorId);
    }


    @Transactional
    public void save(Pretor pretor) { //Testeado
        pretorRepo.save(pretor);
    }


    @Transactional
    public void delete(Pretor pretor){ pretorRepo.delete(pretor);}
}
