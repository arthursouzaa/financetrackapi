package com.financetrack.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JoinColumn(name = "meta_financeira_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MetaFinanceira metaFinanceira;
}
