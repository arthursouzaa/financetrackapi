package com.financetrack.demo.model.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data

public class CategoriaDespesa extends CategoriaLancamento {
    private boolean limiteGasto;
    private float valorLimite;
}
