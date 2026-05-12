package com.financetrack.api.controller;

import com.financetrack.api.dto.ReceitaDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Cliente;
import com.financetrack.model.entity.Receita;
import com.financetrack.service.ClienteService;
import com.financetrack.service.ReceitaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/receitas")
@RequiredArgsConstructor
@CrossOrigin

public class ReceitaController {
    private final ReceitaService service;
    private final ClienteService clienteService;

    @GetMapping()
    public ResponseEntity get() {
        List<Receita> receitas = service.getReceitas();
        return ResponseEntity.ok(receitas.stream().map(ReceitaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Receita> receita = service.getReceitaById(id);
        if (!receita.isPresent()) {
            return new ResponseEntity("Receita não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(receita.map(ReceitaDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ReceitaDTO dto) {
        try {
            Receita receita = converter(dto);
            receita = service.salvar(receita);
            return new ResponseEntity(receita, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ReceitaDTO dto) {
        if (!service.getReceitaById(id).isPresent()) {
            return new ResponseEntity("Receita não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Receita receita = converter(dto);
            receita.setId(id);
            service.salvar(receita);
            return ResponseEntity.ok(receita);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Receita converter(ReceitaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Receita receita = modelMapper.map(dto, Receita.class);
        receita.setId(null);
        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                receita.setCliente(null);
            } else {
                receita.setCliente(cliente.get());
            }
        }
        return receita;
    }
}
