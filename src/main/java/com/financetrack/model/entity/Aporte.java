package com.financetrack.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data 
@AllArgsConstructor
@NoArgsConstructor

public class Aporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float valor;
    private Date dataEnvio;

    @ManyToOne
    private MetaFinanceira metaFinanceira;
}
