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
@RequestMapping("/api/v1/despesas")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Despesas", description = "API de Gerenciamento de Despesas")

public class DespesaController {
    private final DespesaService service;
    private final ClienteService clienteService;
    private final FormaPagamentoService formaPagamentoService;
    private final CategoriaDespesaService categoriaDespesaService;

    @GetMapping()
    @Operation(summary = "Listar todas as Despesas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de Despesas retornada com sucesso!")
    })
    public ResponseEntity get() {
        List<Despesa> despesas = service.getDespesas();
        return ResponseEntity.ok(despesas.stream().map(DespesaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes da Despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Despesa encontrada!"),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada.")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Despesa> despesa = service.getDespesaById(id);
        if (!despesa.isPresent()) {
            return new ResponseEntity("Despesa não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(despesa.map(DespesaDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastrar nova Despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Despesa cadastrada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao Cadastrar Despesa.")
    })
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
    @Operation(summary = "Atualizar Despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar Despesa."),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada.")
    })
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
    @Operation(summary = "Excluir Despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Despesa excluída com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada."),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir Despesa.")
    })
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
