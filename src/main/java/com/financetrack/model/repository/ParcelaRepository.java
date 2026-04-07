package com.financetrack.model.repository;

import com.financetrack.model.entity.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
}
