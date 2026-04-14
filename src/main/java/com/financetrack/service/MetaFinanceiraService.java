package com.financetrack.service;

import com.financetrack.model.entity.MetaFinanceira;
import com.financetrack.model.repository.MetaFinanceiraRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MetaFinanceiraService {
    private MetaFinanceiraRepository repository;

    public MetaFinanceiraService(MetaFinanceiraRepository repository) {
        this.repository = repository;
    }

    public List<MetaFinanceira> getMetasFinanceiras() {
        return repository.findAll();
    }

    public Optional<MetaFinanceira> getMetaFinanceiraById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public MetaFinanceira salvar(MetaFinanceira metaFinanceira) {
        validar(metaFinanceira);
        return repository.save(metaFinanceira);
    }

    @Transactional
    public void excluir(MetaFinanceira metaFinanceira) {
        Objects.requireNonNull(metaFinanceira.getId());
        repository.delete(metaFinanceira);
    }
}
