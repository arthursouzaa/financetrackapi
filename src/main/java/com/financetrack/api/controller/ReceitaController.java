package com.financetrack.api.controller;

import com.financetrack.api.dto.ReceitaDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.CategoriaReceita;
import com.financetrack.model.entity.Cliente;
import com.financetrack.model.entity.Receita;
import com.financetrack.service.CategoriaReceitaService;
import com.financetrack.service.ClienteService;
import com.financetrack.service.ReceitaService;
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
@RequestMapping("/api/v1/receitas")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Receitas", description = "API de Gerenciamento de Receitas")

public class ReceitaController {
    private final ReceitaService service;
    private final ClienteService clienteService;
    private final CategoriaReceitaService categoriaReceitaService;

    @GetMapping()
    @Operation(summary = "Listar todas as Receitas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de Receitas retornada com sucesso!")
    })
    public ResponseEntity get() {
        List<Receita> receitas = service.getReceitas();
        return ResponseEntity.ok(receitas.stream().map(ReceitaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes da Receita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receita encontrada!"),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada.")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Receita> receita = service.getReceitaById(id);
        if (!receita.isPresent()) {
            return new ResponseEntity("Receita não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(receita.map(ReceitaDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastrar nova Receita")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Receita cadastrada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao Cadastrar Receita.")
    })
    public ResponseEntity post(@RequestBody ReceitaDTO dto) {
        try {
            Receita receita = converter(dto);
            receita = service.salvar(receita);
            return new ResponseEntity(receita, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar Receita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receita atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar Receita."),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada.")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ReceitaDTO dto) {
        if (!service.getReceitaById(id).isPresent()) {
            return new ResponseEntity("Receita não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Receita receita = converter(dto);
            receita.setId(id);
            service.salvar(receita);
            return ResponseEntity.ok(receita);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir Receita")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Receita excluída com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada."),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir Receita.")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Receita> receita = service.getReceitaById(id);
        if (!receita.isPresent()) {
            return new ResponseEntity("Receita não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(receita.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
        
    public Receita converter(ReceitaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Receita receita = modelMapper.map(dto, Receita.class);
        receita.setId(null);
        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                receita.setCliente(null);
            } else {
                receita.setCliente(cliente.get());
            }
        }
        if (dto.getIdCategoriaReceita() != null) {
            Optional<CategoriaReceita> categoriaReceita = categoriaReceitaService.getCategoriaReceitaById(dto.getIdCategoriaReceita());
            if (!categoriaReceita.isPresent()) {
                receita.setCategoriaReceita(null);
            } else {
                receita.setCategoriaReceita(categoriaReceita.get());
            }
        }
        return receita;
    }
}
