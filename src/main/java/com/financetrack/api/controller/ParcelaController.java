package com.financetrack.api.controller;

import com.financetrack.api.dto.ParcelaDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Despesa;
import com.financetrack.model.entity.Parcela;
import com.financetrack.service.DespesaService;
import com.financetrack.service.ParcelaService;
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
@RequestMapping("/api/v1/parcelas")
@RequiredArgsConstructor
@Tag(name = "Parcelas", description = "API de Gerenciamento de Parcelas")

public class ParcelaController {
    private final ParcelaService service;
    private final DespesaService despesaService;

    @GetMapping()
    @Operation(summary = "Listar todas as Parcelas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de Parcelas retornada com sucesso!")
    })
    public ResponseEntity get() {
        List<Parcela> parcelas = service.getParcelas();
        return ResponseEntity.ok(parcelas.stream().map(ParcelaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes da Parcela")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parcela encontrada!"),
            @ApiResponse(responseCode = "404", description = "Parcela não encontrada.")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Parcela> parcela = service.getParcelaById(id);
        if (!parcela.isPresent()) {
            return new ResponseEntity("Parcela não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(parcela.map(ParcelaDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastrar nova Parcela")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Parcela cadastrada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao Cadastrar Parcela.")
    })
    public ResponseEntity post(@RequestBody ParcelaDTO dto) {
        try {
            Parcela parcela = converter(dto);
            parcela = service.salvar(parcela);
            return new ResponseEntity(parcela, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar Parcela")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parcela atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar Parcela."),
            @ApiResponse(responseCode = "404", description = "Parcela não encontrada.")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ParcelaDTO dto) {
        if (!service.getParcelaById(id).isPresent()) {
            return new ResponseEntity("Parcela não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Parcela parcela = converter(dto);
            parcela.setId(id);
            service.salvar(parcela);
            return ResponseEntity.ok(parcela);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir Parcela")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Parcela excluída com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Parcela não encontrada."),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir Parcela.")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Parcela> parcela = service.getParcelaById(id);
        if (!parcela.isPresent()) {
            return new ResponseEntity("Parcela não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(parcela.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Parcela converter(ParcelaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Parcela parcela = modelMapper.map(dto, Parcela.class);
        if (dto.getIdDespesa() != null) {
            Optional<Despesa> despesa = despesaService.getDespesaById(dto.getIdDespesa());
            if (!despesa.isPresent()) {
                parcela.setDespesa(null);
            } else {
                parcela.setDespesa(despesa.get());
            }
        }
        return parcela;
    }
}
