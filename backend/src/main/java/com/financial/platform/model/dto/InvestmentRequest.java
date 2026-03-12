package com.financial.platform.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for adding an investment to a portfolio.
 */
public class InvestmentRequest {

    @NotBlank(message = "Asset name is required")
    @Size(max = 200)
    private String assetName;

    @Size(max = 50)
    private String assetType;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private LocalDate purchaseDate;

    public InvestmentRequest() {
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
