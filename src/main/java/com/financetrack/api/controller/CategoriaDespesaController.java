package com.financetrack.api.controller;

import com.financetrack.api.dto.CategoriaDespesaDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.CategoriaDespesa;
import com.financetrack.model.entity.Cliente;
import com.financetrack.service.CategoriaDespesaService;
import com.financetrack.service.ClienteService;
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
@RequestMapping("/api/v1/categoriasDespesa")
@RequiredArgsConstructor
@Tag(name = "Categorias de Despesa", description = "API de Gerenciamento de Categorias de Despesa")

public class CategoriaDespesaController {
    private final CategoriaDespesaService service;
    private final ClienteService clienteService;

    @GetMapping()
    @Operation(summary = "Listar todas as Categorias de Despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de Categorias de Despesa retornada com sucesso!")
    })
    public ResponseEntity get() {
        List<CategoriaDespesa> categoriaDespesas = service.getCategoriasDespesa();
        return ResponseEntity.ok(categoriaDespesas.stream().map(CategoriaDespesaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes da Categoria de Despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria de Despesa encontrada!"),
            @ApiResponse(responseCode = "404", description = "Categoria de Despesa não encontrada.")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<CategoriaDespesa> categoriaDespesa = service.getCategoriaDespesaById(id);
        if (!categoriaDespesa.isPresent()) {
            return new ResponseEntity("Categoria de Despesa não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoriaDespesa.map(CategoriaDespesaDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastrar nova Categoria de Despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria de Despesa cadastrada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao Cadastrar Categoria de Despesa.")
    })
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
    @Operation(summary = "Atualizar Categoria de Despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria de Despesa atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar Categoria de Despesa."),
            @ApiResponse(responseCode = "404", description = "Categoria de Despesa não encontrada.")
    })
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
    @Operation(summary = "Excluir Categoria de Despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria de Despesa excluída com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Categoria de Despesa não encontrada."),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir Categoria de Despesa.")
    })
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
