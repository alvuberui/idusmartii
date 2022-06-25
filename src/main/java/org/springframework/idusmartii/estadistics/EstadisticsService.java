package org.springframework.idusmartii.estadistics;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstadisticsService {


	@Autowired
	private EstadisticsRepository estadisticsRepo;

	@Transactional(readOnly=true)
	public Optional<Estadistics> getEstadisticsById(int estadisticsId) {
		return estadisticsRepo.findById(estadisticsId);
	}

	@Transactional(readOnly = true)
	public Iterable<Estadistics> findAll() {
		return estadisticsRepo.findAll();
	}



	@Transactional
	public void save(Estadistics estadistic) {
		estadisticsRepo.save(estadistic);

	}
}
