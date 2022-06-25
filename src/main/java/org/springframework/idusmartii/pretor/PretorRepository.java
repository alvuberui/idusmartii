package org.springframework.idusmartii.pretor;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PretorRepository extends CrudRepository<Pretor, CrudRepository> {

    Optional<Pretor> findPretorById(int pretorId);
}
