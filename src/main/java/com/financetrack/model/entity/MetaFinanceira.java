package com.financetrack.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @JoinColumn(name = "cliente_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cliente cliente;

    @OneToMany(mappedBy = "metaFinanceira", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aporte> aportes = new ArrayList<>();
}
