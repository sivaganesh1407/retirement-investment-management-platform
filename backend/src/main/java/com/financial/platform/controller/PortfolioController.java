package com.financial.platform.controller;

import com.financial.platform.model.Portfolio;
import com.financial.platform.model.dto.PortfolioRequest;
import com.financial.platform.service.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for portfolio management. All endpoints require authentication.
 * POST /portfolio, GET /portfolio/{customerId}
 */
@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping
    public ResponseEntity<Portfolio> create(@Valid @RequestBody PortfolioRequest request) {
        Portfolio created = portfolioService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<Portfolio>> getByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(portfolioService.findByCustomerId(customerId));
    }
}
