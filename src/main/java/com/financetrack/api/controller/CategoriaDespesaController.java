package com.financetrack.api.controller;

import com.financetrack.api.dto.CategoriaDespesaDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.CategoriaDespesa;
import com.financetrack.service.CategoriaDespesaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categoriasDespesa")
@RequiredArgsConstructor
@CrossOrigin

public class CategoriaDespesaController {
    private final CategoriaDespesaService service;

    @GetMapping()
    public ResponseEntity get() {
        List<CategoriaDespesa> categoriaDespesas = service.getCategoriasDespesa();
        return ResponseEntity.ok(categoriaDespesas.stream().map(CategoriaDespesaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<CategoriaDespesa> categoriaDespesa = service.getCategoriaDespesaById(id);
        if (!categoriaDespesa.isPresent()) {
            return new ResponseEntity("Categoria de Despesa não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoriaDespesa.map(CategoriaDespesaDTO::create));
    }
}
