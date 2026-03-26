package com.financetrack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AporteDTO {
    private Long id;
    private float valor;
    private Date dataEnvio;
    private Long idMetaFinanceira;
}
