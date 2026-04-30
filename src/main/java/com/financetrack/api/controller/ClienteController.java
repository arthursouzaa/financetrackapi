package com.financetrack.api.controller;

import com.financetrack.api.dto.ClienteDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Cliente;
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
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@CrossOrigin

public class ClienteController {
    private final ClienteService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Cliente> categorias = service.getClientes();
        return ResponseEntity.ok(categorias.stream().map(ClienteDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Cliente> categoria = service.getClienteById(id);
        if (!categoria.isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoria.map(ClienteDTO::create));
    }
}
