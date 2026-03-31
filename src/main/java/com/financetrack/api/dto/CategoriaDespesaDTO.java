package com.financetrack.api.dto;

import com.financetrack.model.entity.CategoriaDespesa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CategoriaDespesaDTO {
    private Long id;
    private String nome;
    private boolean limiteGasto;
    private float valorLimite;
    private Long idCliente;

    public static CategoriaDespesaDTO create(CategoriaDespesa categoriaDespesa) {
        ModelMapper modelMapper = new ModelMapper();
        CategoriaDespesaDTO dto = modelMapper.map(categoriaDespesa, CategoriaDespesaDTO.class);
        return dto;
    }
}
