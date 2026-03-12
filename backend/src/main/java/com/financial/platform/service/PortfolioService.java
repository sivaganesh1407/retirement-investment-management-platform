package com.financial.platform.service;

import com.financial.platform.model.Portfolio;
import com.financial.platform.model.dto.PortfolioRequest;
import com.financial.platform.repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for portfolio management: create portfolio and list by customer.
 */
@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional
    public Portfolio create(PortfolioRequest request) {
        Portfolio portfolio = new Portfolio(request.getCustomerId(), request.getPortfolioName());
        return portfolioRepository.save(portfolio);
    }

    @Transactional(readOnly = true)
    public List<Portfolio> findByCustomerId(Long customerId) {
        return portfolioRepository.findByCustomerId(customerId);
    }
}
