package com.financetrack.api.dto;

import com.financetrack.model.entity.FormaPagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FormaPagamentoDTO {
    private Long id;
    private String nome;
    private Long idCliente;

    public static FormaPagamentoDTO create(FormaPagamento formaPagamento) {
        ModelMapper modelMapper = new ModelMapper();
        FormaPagamentoDTO dto = modelMapper.map(formaPagamento, FormaPagamentoDTO.class);
        return dto;
    }
}
