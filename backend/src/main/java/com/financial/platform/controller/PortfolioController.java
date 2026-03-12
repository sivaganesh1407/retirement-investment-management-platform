package com.financial.platform.controller;

import com.financial.platform.model.Investment;
import com.financial.platform.model.Portfolio;
import com.financial.platform.model.dto.InvestmentRequest;
import com.financial.platform.model.dto.PortfolioRequest;
import com.financial.platform.service.InvestmentService;
import com.financial.platform.service.PortfolioService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for portfolio and investment management. All endpoints require authentication.
 * POST /portfolio, GET /portfolio/customer/{customerId}, GET/POST /portfolio/{portfolioId}/investments
 */
@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final InvestmentService investmentService;

    public PortfolioController(PortfolioService portfolioService, InvestmentService investmentService) {
        this.portfolioService = portfolioService;
        this.investmentService = investmentService;
    }

    @PostMapping
    public ResponseEntity<Portfolio> create(@Valid @RequestBody PortfolioRequest request) {
        Portfolio created = portfolioService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Portfolio>> getByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(portfolioService.findByCustomerId(customerId));
    }

    @GetMapping("/{portfolioId}/investments")
    public ResponseEntity<List<Investment>> getInvestments(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(investmentService.findByPortfolioId(portfolioId));
    }

    @PostMapping("/{portfolioId}/investments")
    public ResponseEntity<Investment> addInvestment(
            @PathVariable Long portfolioId,
            @Valid @RequestBody InvestmentRequest request) {
        Investment created = investmentService.addToPortfolio(portfolioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
