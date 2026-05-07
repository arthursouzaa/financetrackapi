package com.financetrack.api.controller;

import com.financetrack.api.dto.ParcelaDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Despesa;
import com.financetrack.model.entity.Parcela;
import com.financetrack.service.DespesaService;
import com.financetrack.service.ParcelaService;
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
@CrossOrigin

public class ParcelaController {
    private final ParcelaService service;
    private final DespesaService despesaService;

    @GetMapping()
    public ResponseEntity get() {
        List<Parcela> parcelas = service.getParcelas();
        return ResponseEntity.ok(parcelas.stream().map(ParcelaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Parcela> parcela = service.getParcelaById(id);
        if (!parcela.isPresent()) {
            return new ResponseEntity("Parcela não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(parcela.map(ParcelaDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ParcelaDTO dto) {
        try {
            Parcela parcela = converter(dto);
            parcela = service.salvar(parcela);
            return new ResponseEntity(parcela, HttpStatus.CREATED);
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
