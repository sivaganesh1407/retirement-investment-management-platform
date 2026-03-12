package com.financial.platform.service;

import com.financial.platform.model.Customer;
import com.financial.platform.model.Portfolio;
import com.financial.platform.model.dto.RetirementProjectionResponse;
import com.financial.platform.repository.CustomerRepository;
import com.financial.platform.repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Calculates retirement savings projections based on current portfolio value
 * and customer risk profile. Uses a simple compound growth model.
 */
@Service
public class RetirementProjectionService {

    private static final int DEFAULT_RETIREMENT_AGE = 65;
    private static final int YEARS_TO_RETIREMENT_DEFAULT = 25;
    /** Annual growth rates by risk profile (decimal, e.g. 0.05 = 5%) */
    private static final double GROWTH_CONSERVATIVE = 0.04;
    private static final double GROWTH_MODERATE = 0.06;
    private static final double GROWTH_AGGRESSIVE = 0.08;

    private final CustomerRepository customerRepository;
    private final PortfolioRepository portfolioRepository;

    public RetirementProjectionService(CustomerRepository customerRepository,
                                      PortfolioRepository portfolioRepository) {
        this.customerRepository = customerRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional(readOnly = true)
    public RetirementProjectionResponse getProjection(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        BigDecimal currentSavings = sumPortfolioValues(customerId);
        double annualGrowth = getGrowthRateForRiskProfile(customer.getRiskProfile());
        int yearsToRetirement = YEARS_TO_RETIREMENT_DEFAULT;
        // Projected value = currentSavings * (1 + r)^years
        BigDecimal projectedValue = currentSavings.multiply(
                BigDecimal.valueOf(Math.pow(1 + annualGrowth, yearsToRetirement)))
                .setScale(2, RoundingMode.HALF_UP);
        int retirementAge = DEFAULT_RETIREMENT_AGE;

        return new RetirementProjectionResponse(currentSavings, projectedValue, retirementAge);
    }

    private BigDecimal sumPortfolioValues(Long customerId) {
        List<Portfolio> portfolios = portfolioRepository.findByCustomerIdWithInvestments(customerId);
        BigDecimal total = BigDecimal.ZERO;
        for (Portfolio p : portfolios) {
            total = total.add(p.getInvestments().stream()
                    .map(inv -> inv.getAmount() != null ? inv.getAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private double getGrowthRateForRiskProfile(String riskProfile) {
        if (riskProfile == null) {
            return GROWTH_MODERATE;
        }
        switch (riskProfile.toUpperCase()) {
            case "CONSERVATIVE":
                return GROWTH_CONSERVATIVE;
            case "AGGRESSIVE":
                return GROWTH_AGGRESSIVE;
            default:
                return GROWTH_MODERATE;
        }
    }
}
