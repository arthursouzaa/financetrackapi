package com.financetrack.service;

import com.financetrack.model.entity.Parcela;
import com.financetrack.model.repository.ParcelaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParcelaService {
    private ParcelaRepository repository;

    public ParcelaService(ParcelaRepository repository) {
        this.repository = repository;
    }

    public List<Parcela> getParcelas() {
        return repository.findAll();
    }

    public Optional<Parcela> getParcelaById(Long id) {
        return repository.findById(id);
    }
}
