package com.financetrack.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data

public class Receita extends Lancamento {
    @ManyToOne
    @JoinColumn(name = "categoria_receita_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CategoriaReceita categoriaReceita;
}
