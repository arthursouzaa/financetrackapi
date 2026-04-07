package com.financetrack.model.repository;

import com.financetrack.model.entity.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
}
