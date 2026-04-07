package com.financetrack.model.repository;

import com.financetrack.model.entity.MetaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetaFinanceiraRepository extends JpaRepository<MetaFinanceira, Long> {
}
