package com.financetrack.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data

public class Despesa extends Lancamento {
    private float quantidadeParcelas;
    private boolean parcelada;

    @ManyToOne
    private CategoriaDespesa categoriaDespesa;

    @ManyToOne
    private FormaPagamento formaPagamento;
}
