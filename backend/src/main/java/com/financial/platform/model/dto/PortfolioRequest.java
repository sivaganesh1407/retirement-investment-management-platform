package com.financial.platform.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a portfolio.
 */
public class PortfolioRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Portfolio name is required")
    @Size(max = 200)
    private String portfolioName;

    public PortfolioRequest() {
    }

    public PortfolioRequest(Long customerId, String portfolioName) {
        this.customerId = customerId;
        this.portfolioName = portfolioName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }
}
