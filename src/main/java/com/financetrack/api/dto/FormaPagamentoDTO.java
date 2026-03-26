package com.financetrack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FormaPagamentoDTO {
    private Long id;
    private String nome;
    private Long idCliente;
}
