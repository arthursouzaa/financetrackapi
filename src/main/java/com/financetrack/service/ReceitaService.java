package com.financetrack.service;

import com.financetrack.model.entity.Receita;
import com.financetrack.model.repository.ReceitaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    @Transactional
    public Receita salvar(Receita receita) {
        validar(receita);
        return repository.save(receita);
    }

    @Transactional
    public void excluir(Receita receita) {
        Objects.requireNonNull(receita.getId());
        repository.delete(receita);
    }
}
