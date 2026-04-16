package com.financetrack.service;

import com.financetrack.api.exception.RegraNegocioException;
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

    public void validar(Receita receita) {
        if (receita.getNome() == null || receita.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome inválido.");
        }
        if (receita.getData() == null) {
            throw new RegraNegocioException("Data inválida.");
        }
        if(receita.getValor() <= 0) {
            throw new RegraNegocioException("Valor inválido!");
        }
    }
}
