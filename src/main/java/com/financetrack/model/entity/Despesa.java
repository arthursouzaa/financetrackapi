package com.financetrack.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data

public class Despesa extends Lancamento {
    private float quantidadeParcelas;
    private boolean parcelada;

    @ManyToOne
    @JoinColumn(name = "categoria_despesa_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CategoriaDespesa categoriaDespesa;

    @ManyToOne
    @JoinColumn(name = "forma_pagamento_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FormaPagamento formaPagamento;
}
