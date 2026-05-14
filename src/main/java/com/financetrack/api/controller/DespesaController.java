package com.financetrack.api.controller;

import com.financetrack.api.dto.DespesaDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.CategoriaDespesa;
import com.financetrack.model.entity.Cliente;
import com.financetrack.model.entity.Despesa;
import com.financetrack.model.entity.FormaPagamento;
import com.financetrack.service.CategoriaDespesaService;
import com.financetrack.service.ClienteService;
import com.financetrack.service.DespesaService;
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
@RequestMapping("/api/v1/despesas")
@RequiredArgsConstructor
@CrossOrigin

public class DespesaController {
    private final DespesaService service;
    private final ClienteService clienteService;
    private final FormaPagamentoService formaPagamentoService;
    private final CategoriaDespesaService categoriaDespesaService;

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

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Despesa> despesa = service.getDespesaById(id);
        if (!despesa.isPresent()) {
            return new ResponseEntity("Despesa não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(despesa.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
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
        if (dto.getIdFormaPagamento() != null) {
            Optional<FormaPagamento> formaPagamento = formaPagamentoService.getFormaPagamentoById(dto.getIdFormaPagamento());
            if (!formaPagamento.isPresent()) {
                despesa.setFormaPagamento(null);
            } else {
                despesa.setFormaPagamento(formaPagamento.get());
            }
        }
        if (dto.getIdCategoriaDespesa() != null) {
            Optional<CategoriaDespesa> categoriaDespesa = categoriaDespesaService.getCategoriaDespesaById(dto.getIdCategoriaDespesa());
            if (!categoriaDespesa.isPresent()) {
                despesa.setCategoriaDespesa(null);
            } else {
                despesa.setCategoriaDespesa(categoriaDespesa.get());
            }
        }
        return despesa;
    }
}
