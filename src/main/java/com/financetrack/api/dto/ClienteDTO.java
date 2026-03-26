package com.financetrack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClienteDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private String senhaConfirmada;
}