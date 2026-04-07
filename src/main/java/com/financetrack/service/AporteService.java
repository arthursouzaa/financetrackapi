package com.financetrack.service;

import com.financetrack.model.entity.Aporte;
import com.financetrack.model.repository.AporteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AporteService {
    private AporteRepository repository;

    public AporteService(AporteRepository repository) {
        this.repository = repository;
    }

    public List<Aporte> getAportes() {
        return repository.findAll();
    }

    public Optional<Aporte> getAporteById(Long id) {
        return repository.findById(id);
    }
}
