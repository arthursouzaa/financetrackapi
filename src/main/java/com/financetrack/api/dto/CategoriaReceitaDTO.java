package com.financetrack.api.dto;

import com.financetrack.model.entity.CategoriaReceita;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CategoriaReceitaDTO {
    private Long id;
    private String nome;
    private Long idCliente;

    public static CategoriaReceitaDTO create(CategoriaReceita categoriaReceita) {
        ModelMapper modelMapper = new ModelMapper();
        CategoriaReceitaDTO dto = modelMapper.map(categoriaReceita, CategoriaReceitaDTO.class);
        return dto;
    }
}
