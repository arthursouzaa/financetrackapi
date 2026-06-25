package com.financetrack.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data

public class CategoriaDespesa extends CategoriaLancamento {
    private boolean limiteGasto;
    private float valorLimite;

    @OneToMany(mappedBy = "categoriaDespesa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Despesa> despesas = new ArrayList<>();
}
