package com.financetrack.api.dto;

import com.financetrack.model.entity.Parcela;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ParcelaDTO {
    private Long id;
    private float valorParcela;
    private boolean pago;
    private Long idCliente;
    private Long idDespesa;

    public static ParcelaDTO create(Parcela parcela) {
        ModelMapper modelMapper = new ModelMapper();
        ParcelaDTO dto = modelMapper.map(parcela, ParcelaDTO.class);
        return dto;
    }
}
