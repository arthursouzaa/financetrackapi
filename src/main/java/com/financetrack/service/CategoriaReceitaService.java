package com.financetrack.service;

import com.financetrack.model.entity.CategoriaReceita;
import com.financetrack.model.repository.CategoriaReceitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaReceitaService {
    private CategoriaReceitaRepository repository;

    public CategoriaReceitaService(CategoriaReceitaRepository repository) {
        this.repository = repository;
    }

    public List<CategoriaReceita> getCategoriasReceita() {
        return repository.findAll();
    }

    public Optional<CategoriaReceita> getCategoriaReceitaById(Long id) {
        return repository.findById(id);
    }
}
