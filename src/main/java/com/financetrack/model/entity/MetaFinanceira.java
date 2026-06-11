package com.financetrack.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MetaFinanceira {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private float valor;
    private Date dataEnvio;
    private Date dataAlvo;
    private float investimentoInicial;
    private boolean status;

    @ManyToOne
    private Cliente cliente;
}
