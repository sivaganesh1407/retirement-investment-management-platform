package com.financial.platform.repository;

import com.financial.platform.model.Investment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Data access for Investment entity.
 */
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    List<Investment> findByPortfolioId(Long portfolioId);
}
