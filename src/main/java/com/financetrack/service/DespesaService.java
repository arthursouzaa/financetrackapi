package com.financetrack.service;

import com.financetrack.model.entity.Despesa;
import com.financetrack.model.repository.DespesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DespesaService {
    private DespesaRepository repository;

    public DespesaService(DespesaRepository repository) {
        this.repository = repository;
    }

    public List<Despesa> getDespesas() {
        return repository.findAll();
    }

    public Optional<Despesa> getDespesaById(Long id) {
        return repository.findById(id);
    }
}
