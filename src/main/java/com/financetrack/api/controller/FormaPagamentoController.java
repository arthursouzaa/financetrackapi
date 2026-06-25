package com.financetrack.api.controller;

import com.financetrack.api.dto.FormaPagamentoDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Cliente;
import com.financetrack.model.entity.FormaPagamento;
import com.financetrack.model.entity.MetaFinanceira;
import com.financetrack.service.ClienteService;
import com.financetrack.service.FormaPagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Formas de Pagamento", description = "API de Gerenciamento de Formas de Pagamento")

public class FormaPagamentoController {
    private final FormaPagamentoService service;
    private final ClienteService clienteService;

    @GetMapping()
    @Operation(summary = "Listar todas as Formas de Pagamento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de Formas de Pagamento retornada com sucesso!")
    })
    public ResponseEntity get() {
        List<FormaPagamento> categorias = service.getFormasPagamento();
        return ResponseEntity.ok(categorias.stream().map(FormaPagamentoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes da Forma de Pagamento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Forma de Pagamento encontrada!"),
            @ApiResponse(responseCode = "404", description = "Forma de Pagamento não encontrada.")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<FormaPagamento> categoria = service.getFormaPagamentoById(id);
        if (!categoria.isPresent()) {
            return new ResponseEntity("Forma de Pagamento não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoria.map(FormaPagamentoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastrar nova Forma de Pagamento")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Forma de Pagamento cadastrada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao Cadastrar Forma de Pagamento.")
    })
    public ResponseEntity post(@RequestBody FormaPagamentoDTO dto) {
        try {
            FormaPagamento formaPagamento = converter(dto);
            formaPagamento = service.salvar(formaPagamento);
            return new ResponseEntity(formaPagamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar Forma de Pagamento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Forma de Pagamento atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar Forma de Pagamento."),
            @ApiResponse(responseCode = "404", description = "Forma de Pagamento não encontrada.")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody FormaPagamentoDTO dto) {
        if (!service.getFormaPagamentoById(id).isPresent()) {
            return new ResponseEntity("Forma de pagamento não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            FormaPagamento formaPagamento = converter(dto);
            formaPagamento.setId(id);
            service.salvar(formaPagamento);
            return ResponseEntity.ok(formaPagamento);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir Forma de Pagamento")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Forma de Pagamento excluída com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Forma de Pagamento não encontrada."),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir Forma de Pagamento.")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<FormaPagamento> formaPagamento = service.getFormaPagamentoById(id);
        if (!formaPagamento.isPresent()) {
            return new ResponseEntity("Forma de pagamento não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(formaPagamento.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
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
