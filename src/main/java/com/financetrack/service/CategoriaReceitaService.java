package com.financetrack.service;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.CategoriaReceita;
import com.financetrack.model.repository.CategoriaReceitaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    @Transactional
    public CategoriaReceita salvar(CategoriaReceita categoriaReceita) {
        validar(categoriaReceita);
        return repository.save(categoriaReceita);
    }

    @Transactional
    public void excluir(CategoriaReceita categoriaReceita) {
        Objects.requireNonNull(categoriaReceita.getId());
        repository.delete(categoriaReceita);
    }

    public void validar(CategoriaReceita categoriaReceita) {
        if (categoriaReceita.getNome() == null || categoriaReceita.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome inválido.");
        }
    }
}
