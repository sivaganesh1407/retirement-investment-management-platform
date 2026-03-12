package com.financial.platform.service;

import com.financial.platform.model.Investment;
import com.financial.platform.model.dto.InvestmentRequest;
import com.financial.platform.repository.InvestmentRepository;
import com.financial.platform.repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for investment management: add investment to portfolio, list by portfolio.
 */
@Service
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final PortfolioRepository portfolioRepository;

    public InvestmentService(InvestmentRepository investmentRepository,
                             PortfolioRepository portfolioRepository) {
        this.investmentRepository = investmentRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional
    public Investment addToPortfolio(Long portfolioId, InvestmentRequest request) {
        if (!portfolioRepository.existsById(portfolioId)) {
            throw new IllegalArgumentException("Portfolio not found: " + portfolioId);
        }
        Investment investment = new Investment(
                portfolioId,
                request.getAssetName(),
                request.getAssetType(),
                request.getAmount(),
                request.getPurchaseDate());
        return investmentRepository.save(investment);
    }

    @Transactional(readOnly = true)
    public List<Investment> findByPortfolioId(Long portfolioId) {
        return investmentRepository.findByPortfolioId(portfolioId);
    }
}
