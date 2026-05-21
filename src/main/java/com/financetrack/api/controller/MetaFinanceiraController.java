package com.financetrack.api.controller;

import com.financetrack.api.dto.MetaFinanceiraDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Cliente;
import com.financetrack.model.entity.MetaFinanceira;
import com.financetrack.service.ClienteService;
import com.financetrack.service.MetaFinanceiraService;
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
@RequestMapping("/api/v1/metasFinanceiras")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Metas Financeiras", description = "API de Gerenciamento de Metas Financeiras")

public class MetaFinanceiraController {
    private final MetaFinanceiraService service;
    private final ClienteService clienteService;

    @GetMapping()
    @Operation(summary = "Listar todas as Metas Financeiras")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de Metas Financeiras retornada com sucesso!")
    })
    public ResponseEntity get() {
        List<MetaFinanceira> metasFinanceira = service.getMetasFinanceiras();
        return ResponseEntity.ok(metasFinanceira.stream().map(MetaFinanceiraDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes da Meta Financeira")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meta Financeira encontrada!"),
            @ApiResponse(responseCode = "404", description = "Meta Financeira não encontrada.")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<MetaFinanceira> metaFinanceira = service.getMetaFinanceiraById(id);
        if (!metaFinanceira.isPresent()) {
            return new ResponseEntity("Meta Financeira não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(metaFinanceira.map(MetaFinanceiraDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastrar nova Meta Financeira")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Meta Financeira cadastrada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao Cadastrar Meta Financeira.")
    })
    public ResponseEntity post(@RequestBody MetaFinanceiraDTO dto) {
        try {
            MetaFinanceira metaFinanceira = converter(dto);
            metaFinanceira = service.salvar(metaFinanceira);
            return new ResponseEntity(metaFinanceira, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar Meta Financeira")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meta Financeira atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar Meta Financeira."),
            @ApiResponse(responseCode = "404", description = "Meta Financeira não encontrada.")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody MetaFinanceiraDTO dto) {
        if (!service.getMetaFinanceiraById(id).isPresent()) {
            return new ResponseEntity("Meta financeira não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            MetaFinanceira metaFinanceira = converter(dto);
            metaFinanceira.setId(id);
            service.salvar(metaFinanceira);
            return ResponseEntity.ok(metaFinanceira);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir Meta Financeira")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Meta Financeira excluída com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Meta Financeira não encontrada."),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir Meta Financeira.")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<MetaFinanceira> metaFinanceira = service.getMetaFinanceiraById(id);
        if (!metaFinanceira.isPresent()) {
            return new ResponseEntity("Meta financeira não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(metaFinanceira.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
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