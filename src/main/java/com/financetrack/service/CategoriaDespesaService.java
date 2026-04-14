package com.financetrack.service;

import com.financetrack.model.entity.CategoriaDespesa;
import com.financetrack.model.repository.CategoriaDespesaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    @Transactional
    public CategoriaDespesa salvar(CategoriaDespesa categoriaDespesa) {
        validar(categoriaDespesa);
        return repository.save(categoriaDespesa);
    }

    @Transactional
    public void excluir(CategoriaDespesa categoriaDespesa) {
        Objects.requireNonNull(categoriaDespesa.getId());
        repository.delete(categoriaDespesa);
    }

    public validar(CategoriaDespesa categoriaDespesa) {
        if(categoriaDespesa.getValorLimite() )
    }
}
