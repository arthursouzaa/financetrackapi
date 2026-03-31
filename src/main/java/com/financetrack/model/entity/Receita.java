package com.financetrack.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data

public class Receita extends Lancamento {
    @ManyToOne
    private CategoriaReceita categoriaReceita;
}
