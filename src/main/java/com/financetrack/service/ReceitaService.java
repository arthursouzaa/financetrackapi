package com.financetrack.service;

import com.financetrack.model.entity.Receita;
import com.financetrack.model.repository.ReceitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {
    private ReceitaRepository repository;

    public ReceitaService(ReceitaRepository repository) {
        this.repository = repository;
    }

    public List<Receita> getReceitas() {
        return repository.findAll();
    }

    public Optional<Receita> getReceitaById(Long id) {
        return repository.findById(id);
    }
}
