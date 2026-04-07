package com.financetrack.model.repository;

import com.financetrack.model.entity.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
}
