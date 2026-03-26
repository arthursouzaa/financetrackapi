package com.financetrack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReceitaDTO {
    private String nome;
    private Date data;
    private boolean volume;
    private float valor;
    private Long idCliente;
    private Long idCategoriaReceita;
}
