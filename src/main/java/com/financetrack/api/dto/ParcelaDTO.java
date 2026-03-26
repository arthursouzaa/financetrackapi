package com.financetrack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ParcelaDTO {
    private Long id;
    private float valorParcela;
    private boolean pago;
    private Long idCliente;
    private Long idDespesa;
}
