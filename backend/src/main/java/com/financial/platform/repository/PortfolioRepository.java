package com.financial.platform.repository;

import com.financial.platform.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Data access for Portfolio entity.
 */
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findByCustomerId(Long customerId);

    /** Fetches portfolios with investments loaded (for projection calculations). */
    @Query("SELECT DISTINCT p FROM Portfolio p LEFT JOIN FETCH p.investments WHERE p.customerId = :customerId")
    List<Portfolio> findByCustomerIdWithInvestments(@Param("customerId") Long customerId);
}
