package com.financetrack.api.controller;

import com.financetrack.api.dto.AporteDTO;
import com.financetrack.api.dto.CategoriaDespesaDTO;

import com.financetrack.api.dto.CategoriaReceitaDTO;
import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Aporte;
import com.financetrack.model.entity.CategoriaDespesa;
import com.financetrack.model.entity.CategoriaReceita;
import com.financetrack.model.entity.Cliente;
import com.financetrack.service.CategoriaDespesaService;
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
@RequestMapping("/api/v1/categoriasDespesa")
@RequiredArgsConstructor
@CrossOrigin

public class CategoriaDespesaController {
    private final CategoriaDespesaService service;
    private final ClienteService clienteService;

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

    @PostMapping()
    public ResponseEntity post(@RequestBody CategoriaDespesaDTO dto) {
        try {
            CategoriaDespesa categoriaDespesa = converter(dto);
            categoriaDespesa = service.salvar(categoriaDespesa);
            return new ResponseEntity(categoriaDespesa, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody CategoriaDespesaDTO dto) {
        if (!service.getCategoriaDespesaById(id).isPresent()) {
            return new ResponseEntity("Categoria não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            CategoriaDespesa categoriaDespesa = converter(dto);
            categoriaDespesa.setId(id);
            service.salvar(categoriaDespesa);
            return ResponseEntity.ok(categoriaDespesa);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<CategoriaDespesa> categoriaDespesa = service.getCategoriaDespesaById(id);
        if (!categoriaDespesa.isPresent()) {
            return new ResponseEntity("Categoria não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(categoriaDespesa.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public CategoriaDespesa converter(CategoriaDespesaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        CategoriaDespesa categoriaDespesa = modelMapper.map(dto, CategoriaDespesa.class);
        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                categoriaDespesa.setCliente(null);
            } else {
                categoriaDespesa.setCliente(cliente.get());
            }
        }
        return categoriaDespesa;
    }
}
