package com.financetrack.service;

import com.financetrack.model.entity.FormaPagamento;
import com.financetrack.model.repository.FormaPagamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FormaPagamentoService {
    private FormaPagamentoRepository repository;

    public FormaPagamentoService(FormaPagamentoRepository repository) {
        this.repository = repository;
    }

    public List<FormaPagamento> getFormasPagamento() {
        return repository.findAll();
    }

    public Optional<FormaPagamento> getFormaPagamentoById(Long id) {
        return repository.findById(id);
    }
}
