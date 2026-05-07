package com.financetrack.api.controller;

import com.financetrack.api.dto.CategoriaReceitaDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.CategoriaReceita;
import com.financetrack.model.entity.Cliente;
import com.financetrack.service.CategoriaReceitaService;
import com.financetrack.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categoriasReceita")
@RequiredArgsConstructor
@CrossOrigin

public class CategoriaReceitaController {
    private final CategoriaReceitaService service;
    private final ClienteService clienteService;

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

    @PostMapping()
    public ResponseEntity post(@RequestBody CategoriaReceitaDTO dto) {
        try {
            CategoriaReceita categoriaReceita = converter(dto);
            categoriaReceita = service.salvar(categoriaReceita);
            return new ResponseEntity(categoriaReceita, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public CategoriaReceita converter(CategoriaReceitaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        CategoriaReceita categoriaReceita = modelMapper.map(dto, CategoriaReceita.class);
        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                categoriaReceita.setCliente(null);
            } else {
                categoriaReceita.setCliente(cliente.get());
            }
        }
        return categoriaReceita;
    }
}
