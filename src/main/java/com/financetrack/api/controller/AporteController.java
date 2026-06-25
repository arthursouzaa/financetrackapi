package com.financetrack.api.controller;

import com.financetrack.api.dto.AporteDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Aporte;
import com.financetrack.model.entity.MetaFinanceira;
import com.financetrack.service.AporteService;
import com.financetrack.service.MetaFinanceiraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1/aportes")
@RequiredArgsConstructor
@Tag(name = "Aportes", description = "API de Gerenciamento de Aportes")

public class AporteController {
    private final AporteService service;
    private final MetaFinanceiraService metaFinanceiraService;

    @GetMapping()
    @Operation(summary = "Listar todos os Aportes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de Aportes retornada com sucesso!")
    })
    public ResponseEntity get() {
        List<Aporte> aportes = service.getAportes();
        return ResponseEntity.ok(aportes.stream().map(AporteDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes do Aporte")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aporte encontrado!"),
            @ApiResponse(responseCode = "404", description = "Aporte não encontrado.")
    })
    public ResponseEntity get(@PathVariable("id") @Parameter(description = "ID do aporte") Long id) {
        Optional<Aporte> aporte = service.getAporteById(id);
        if (!aporte.isPresent()) {
            return new ResponseEntity("Aporte não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(aporte.map(AporteDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastrar novo Aporte")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Aporte cadastrado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao cadastrar aporte.")
    })
    public ResponseEntity post(@RequestBody AporteDTO dto) {
        try {
            Aporte aporte = converter(dto);
            aporte = service.salvar(aporte);
            return new ResponseEntity(aporte, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar Aporte")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aporte atualizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar Aporte."),
            @ApiResponse(responseCode = "404", description = "Aporte não encontrado.")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody AporteDTO dto) {
        if (!service.getAporteById(id).isPresent()) {
            return new ResponseEntity("Aporte não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Aporte aporte = converter(dto);
            aporte.setId(id);
            service.salvar(aporte);
            return ResponseEntity.ok(aporte);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir Aporte")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Aporte excluído com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Aporte não encontrado."),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir Aporte.")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Aporte> aporte = service.getAporteById(id);
        if (!aporte.isPresent()) {
            return new ResponseEntity("Aporte não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(aporte.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Aporte converter(AporteDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Aporte aporte = modelMapper.map(dto, Aporte.class);
        if (dto.getIdMetaFinanceira() != null) {
            Optional<MetaFinanceira> metaFinanceira = metaFinanceiraService.getMetaFinanceiraById(dto.getIdMetaFinanceira());
            if (!metaFinanceira.isPresent()) {
                aporte.setMetaFinanceira(null);
            } else {
                aporte.setMetaFinanceira(metaFinanceira.get());
            }
        }
        return aporte;
    }
}
