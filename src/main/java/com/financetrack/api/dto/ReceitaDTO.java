package com.financetrack.api.dto;

import com.financetrack.model.entity.Receita;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReceitaDTO {
    private Long id;
    private String nome;
    private Date data;
    private boolean volume;
    private float valor;
    private Long idCliente;
    private Long idCategoriaReceita;
    private String nomeCategoriaReceita;

    public static ReceitaDTO create(Receita receita) {
        ModelMapper modelMapper = new ModelMapper();
        ReceitaDTO dto = modelMapper.map(receita, ReceitaDTO.class);
        dto.nomeCategoriaReceita = receita.getCategoriaReceita() != null ? receita.getCategoriaReceita().getNome() : "Sem Categoria";
        return dto;
    }
}
