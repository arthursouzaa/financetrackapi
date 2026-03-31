package com.financetrack.api.dto;

import com.financetrack.model.entity.Aporte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AporteDTO {
    private Long id;
    private float valor;
    private Date dataEnvio;
    private Long idMetaFinanceira;
    private String nomeMetaFinanceira;

    public static AporteDTO create(Aporte aporte) {
        ModelMapper modelMapper = new ModelMapper();
        AporteDTO dto = modelMapper.map(aporte, AporteDTO.class);
        dto.nomeMetaFinanceira = aporte.getMetaFinanceira().getNome();
        return dto;
    }
}
