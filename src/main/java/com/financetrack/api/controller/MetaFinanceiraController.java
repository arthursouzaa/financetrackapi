package com.financetrack.api.controller;

import com.financetrack.api.dto.MetaFinanceiraDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Cliente;
import com.financetrack.model.entity.MetaFinanceira;
import com.financetrack.service.ClienteService;
import com.financetrack.service.MetaFinanceiraService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/metasFinanceiras")
@RequiredArgsConstructor
@CrossOrigin

public class MetaFinanceiraController {
    private final MetaFinanceiraService service;
    private final ClienteService clienteService;

    @GetMapping()
    public ResponseEntity get() {
        List<MetaFinanceira> metaFinanceiras = service.getMetasFinanceiras();
        return ResponseEntity.ok(metaFinanceiras.stream().map(MetaFinanceiraDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<MetaFinanceira> metaFinanceira = service.getMetaFinanceiraById(id);
        if (!metaFinanceira.isPresent()) {
            return new ResponseEntity("Meta Financeira não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(metaFinanceira.map(MetaFinanceiraDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody MetaFinanceiraDTO dto) {
        try {
            MetaFinanceira metaFinanceira = converter(dto);
            metaFinanceira = service.salvar(metaFinanceira);
            return new ResponseEntity(metaFinanceira, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public MetaFinanceira converter(MetaFinanceiraDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        MetaFinanceira metaFinanceira = modelMapper.map(dto, MetaFinanceira.class);
        metaFinanceira.setId(null);
        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                metaFinanceira.setCliente(null);
            } else {
                metaFinanceira.setCliente(cliente.get());
            }
        }
        return metaFinanceira;
    }
}