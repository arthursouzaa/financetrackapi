package com.financetrack.api.controller;

import com.financetrack.api.dto.FormaPagamentoDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.FormaPagamento;
import com.financetrack.service.FormaPagamentoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/formasPagamento")
@RequiredArgsConstructor
@CrossOrigin

public class FormaPagamentoController {
    private final FormaPagamentoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<FormaPagamento> categorias = service.getFormasPagamento();
        return ResponseEntity.ok(categorias.stream().map(FormaPagamentoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<FormaPagamento> categoria = service.getFormaPagamentoById(id);
        if (!categoria.isPresent()) {
            return new ResponseEntity("Forma de Pagamento não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoria.map(FormaPagamentoDTO::create));
    }
}
