package com.financetrack.api.controller;

import com.financetrack.api.dto.DespesaDTO;

import com.financetrack.model.entity.Despesa;
import com.financetrack.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/despesas")
@RequiredArgsConstructor
@CrossOrigin

public class DespesaController {
    private final DespesaService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Despesa> despesas = service.getDespesas();
        return ResponseEntity.ok(despesas.stream().map(DespesaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Despesa> despesa = service.getDespesaById(id);
        if (!despesa.isPresent()) {
            return new ResponseEntity("Despesa não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(despesa.map(DespesaDTO::create));
    }
}
