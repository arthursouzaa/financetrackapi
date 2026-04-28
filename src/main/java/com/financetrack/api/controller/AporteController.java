package com.financetrack.api.controller;

import com.financetrack.api.dto.AporteDTO;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Aporte;
import com.financetrack.service.AporteService;
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
@CrossOrigin

public class AporteController {
    private final AporteService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Aporte> aportes = service.getAportes();
        return ResponseEntity.ok(aportes.stream().map(AporteDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Aporte> aporte = service.getAporteById(id);
        if (!aporte.isPresent()) {
            return new ResponseEntity("Aporte não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(aporte.map(AporteDTO::create));
    }
}
