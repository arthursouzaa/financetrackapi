package com.financetrack.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data

public class CategoriaReceita extends CategoriaLancamento {
    @OneToMany(mappedBy = "categoriaReceita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Receita> receitas = new ArrayList<>();
}
