package com.financetrack.api.controller;

import com.financetrack.api.dto.FormaPagamentoDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Cliente;
import com.financetrack.model.entity.FormaPagamento;
import com.financetrack.model.entity.MetaFinanceira;
import com.financetrack.service.ClienteService;
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
@RequestMapping("/api/v1/formasPagamento")
@RequiredArgsConstructor
@CrossOrigin

public class FormaPagamentoController {
    private final FormaPagamentoService service;
    private final ClienteService clienteService;

    @GetMapping()
    public ResponseEntity get() {
        List<FormaPagamento> categorias = service.getFormasPagamento();
        return ResponseEntity.ok(categorias.stream().map(FormaPagamentoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<FormaPagamento> categoria = service.getFormaPagamentoById(id);
        if (!categoria.isPresent()) {
            return new ResponseEntity("Forma de Pagamento não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoria.map(FormaPagamentoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody FormaPagamentoDTO dto) {
        try {
            FormaPagamento formaPagamento = converter(dto);
            formaPagamento = service.salvar(formaPagamento);
            return new ResponseEntity(formaPagamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public FormaPagamento converter(FormaPagamentoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        FormaPagamento formaPagamento = modelMapper.map(dto, FormaPagamento.class);
        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                formaPagamento.setCliente(null);
            } else {
                formaPagamento.setCliente(cliente.get());
            }
        }
        return formaPagamento;
    }
}
