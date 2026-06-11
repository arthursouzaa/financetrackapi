package com.financetrack.api.dto;

import com.financetrack.model.entity.Despesa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DespesaDTO {
    private Long id;
    private String nome;
    private Date data;
    private boolean volume;
    private float valor;
    private float quantidadeParcelas;
    private boolean parcelada;
    private Long idCliente;
    private Long idFormaPagamento;
    private String nomeFormaPagamento;
    private Long idCategoriaDespesa;
    private String nomeCategoriaDespesa;

    public static DespesaDTO create(Despesa despesa) {
        ModelMapper modelMapper = new ModelMapper();
        DespesaDTO dto = modelMapper.map(despesa, DespesaDTO.class);
        dto.nomeCategoriaDespesa = despesa.getCategoriaDespesa().getNome();
        dto.nomeFormaPagamento = despesa.getFormaPagamento().getNome();
        return dto;
    }
}
