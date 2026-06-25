package com.financetrack.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.financetrack.model.entity.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClienteDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senhaConfirmada;
    private boolean admin;

    public static ClienteDTO create(Cliente cliente) {
        ModelMapper modelMapper = new ModelMapper();
        ClienteDTO dto = modelMapper.map(cliente, ClienteDTO.class);
        return dto;
    }
}