package com.financetrack.api.controller;

import com.financetrack.api.dto.CategoriaReceitaDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.CategoriaReceita;
import com.financetrack.model.entity.Cliente;
import com.financetrack.service.CategoriaReceitaService;
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
@RequestMapping("/api/v1/categoriasReceita")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Categorias de Receita", description = "API de Gerenciamento de Categorias de Receita")

public class CategoriaReceitaController {
    private final CategoriaReceitaService service;
    private final ClienteService clienteService;

    @GetMapping()
    @Operation(summary = "Listar todas as Categorias de Receita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de Categorias de Receita retornada com sucesso!")
    })
    public ResponseEntity get() {
        List<CategoriaReceita> categoriaReceitas = service.getCategoriasReceita();
        return ResponseEntity.ok(categoriaReceitas.stream().map(CategoriaReceitaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes da Categorias de Receita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categorias de Receita encontrada!"),
            @ApiResponse(responseCode = "404", description = "Categorias de Receita não encontrada.")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<CategoriaReceita> categoriaReceita = service.getCategoriaReceitaById(id);
        if (!categoriaReceita.isPresent()) {
            return new ResponseEntity("Categoria de Receita não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoriaReceita.map(CategoriaReceitaDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastrar nova Categoria de Receita")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria de Receita cadastrada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao Cadastrar Categoria de Receita.")
    })
    public ResponseEntity post(@RequestBody CategoriaReceitaDTO dto) {
        try {
            CategoriaReceita categoriaReceita = converter(dto);
            categoriaReceita = service.salvar(categoriaReceita);
            return new ResponseEntity(categoriaReceita, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar Categoria de Receita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria de Receita atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar Categoria de Receita."),
            @ApiResponse(responseCode = "404", description = "Categoria de Receita não encontrada.")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody CategoriaReceitaDTO dto) {
        if (!service.getCategoriaReceitaById(id).isPresent()) {
            return new ResponseEntity("Categoria não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            CategoriaReceita categoriaReceita = converter(dto);
            categoriaReceita.setId(id);
            service.salvar(categoriaReceita);
            return ResponseEntity.ok(categoriaReceita);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir Categoria de Receita")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria de Receita excluída com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Categoria de Receita não encontrada."),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir Categoria de Receita.")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<CategoriaReceita> categoriaReceita = service.getCategoriaReceitaById(id);
        if (!categoriaReceita.isPresent()) {
            return new ResponseEntity("Categoria não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(categoriaReceita.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public CategoriaReceita converter(CategoriaReceitaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        CategoriaReceita categoriaReceita = modelMapper.map(dto, CategoriaReceita.class);
        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                categoriaReceita.setCliente(null);
            } else {
                categoriaReceita.setCliente(cliente.get());
            }
        }
        return categoriaReceita;
    }
}
