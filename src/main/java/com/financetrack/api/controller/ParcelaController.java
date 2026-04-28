package com.financetrack.api.controller;

import com.financetrack.api.dto.ParcelaDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Parcela;
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
}
