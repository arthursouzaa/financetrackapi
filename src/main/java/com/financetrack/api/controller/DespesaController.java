package com.financetrack.api.controller;

import com.financetrack.api.dto.DespesaDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Cliente;
import com.financetrack.model.entity.Despesa;
import com.financetrack.service.ClienteService;
import com.financetrack.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ClienteService clienteService;

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

    @PostMapping()
    public ResponseEntity post(@RequestBody DespesaDTO dto) {
        try {
            Despesa despesa = converter(dto);
            despesa = service.salvar(despesa);
            return new ResponseEntity(despesa, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody DespesaDTO dto) {
        if (!service.getDespesaById(id).isPresent()) {
            return new ResponseEntity("Despesa não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Despesa despesa = converter(dto);
            despesa.setId(id);
            service.salvar(despesa);
            return ResponseEntity.ok(despesa);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Despesa converter(DespesaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Despesa despesa = modelMapper.map(dto, Despesa.class);
        despesa.setId(null);
        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                despesa.setCliente(null);
            } else {
                despesa.setCliente(cliente.get());
            }
        }
        return despesa;
    }
}
