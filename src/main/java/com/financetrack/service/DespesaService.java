package com.financetrack.service;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Despesa;
import com.financetrack.model.repository.DespesaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    @Transactional
    public Despesa salvar(Despesa despesa) {
        validar(despesa);
        return repository.save(despesa);
    }

    @Transactional
    public void excluir(Despesa despesa) {
        Objects.requireNonNull(despesa.getId());
        repository.delete(despesa);
    }

    public void validar(Despesa despesa) {
        if (despesa.getNome() == null || despesa.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome inválido.");
        }
        if (despesa.getData() == null) {
            throw new RegraNegocioException("Data inválida.");
        }
        if (despesa.getValor() <= 0) {
            throw new RegraNegocioException("Valor inválido!");
        }
        if (despesa.isParcelada() && despesa.getQuantidadeParcelas() <= 0) {
            throw new RegraNegocioException("A quantidade de parcelas deve ser maior que zero.");
        }
    }
}
