package com.financetrack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CategoriaDespesaDTO {
    private Long id;
    private String nome;
    private boolean limiteGasto;
    private float valorLimite;
    private Long idCliente;
}
