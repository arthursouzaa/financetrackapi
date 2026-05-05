package com.financetrack.api.dto;

import com.financetrack.model.entity.MetaFinanceira;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

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
    private boolean status; // adicional: status da meta - em aberto ou concluída
    private Long idCliente;

    public static MetaFinanceiraDTO create(MetaFinanceira metaFinanceira) {
        ModelMapper modelMapper = new ModelMapper();
        MetaFinanceiraDTO dto = modelMapper.map(metaFinanceira, MetaFinanceiraDTO.class);
        dto.idCliente = metaFinanceira.getCliente().getId();
        return dto;
    }
}
