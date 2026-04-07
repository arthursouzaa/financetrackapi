package com.financetrack.service;

import com.financetrack.model.entity.CategoriaDespesa;
import com.financetrack.model.repository.CategoriaDespesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaDespesaService {
    private CategoriaDespesaRepository repository;

    public CategoriaDespesaService(CategoriaDespesaRepository repository) {
        this.repository = repository;
    }

    public List<CategoriaDespesa> getCategoriasDespesa() {
        return repository.findAll();
    }

    public Optional<CategoriaDespesa> getCategoriaDespesaById(Long id) {
        return repository.findById(id);
    }
}
