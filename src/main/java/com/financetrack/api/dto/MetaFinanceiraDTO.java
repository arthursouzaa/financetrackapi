package com.financetrack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
    
public class MetaFinanceiraDTO {
    private String nome;
    private float valor;
    private Date dataEnvio;
    private Date dataAlvo;
    private float investimentoInicial;
    private Long idCliente;
}
