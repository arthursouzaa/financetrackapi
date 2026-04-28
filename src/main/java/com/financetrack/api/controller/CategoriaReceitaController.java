package com.financetrack.api.controller;

import com.financetrack.api.dto.CategoriaReceitaDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.CategoriaReceita;
import com.financetrack.service.CategoriaReceitaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categoriaReceitas")
@RequiredArgsConstructor
@CrossOrigin

public class CategoriaReceitaController {
    private final CategoriaReceitaService service;

    @GetMapping()
    public ResponseEntity get() {
        List<CategoriaReceita> categoriaReceitas = service.getCategoriasReceita();
        return ResponseEntity.ok(categoriaReceitas.stream().map(CategoriaReceitaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<CategoriaReceita> categoriaReceita = service.getCategoriaReceitaById(id);
        if (!categoriaReceita.isPresent()) {
            return new ResponseEntity("Categoria de Receita não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoriaReceita.map(CategoriaReceitaDTO::create));
    }
}
