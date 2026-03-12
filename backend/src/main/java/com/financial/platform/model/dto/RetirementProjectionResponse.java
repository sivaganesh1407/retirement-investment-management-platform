package com.financial.platform.model.dto;

import java.math.BigDecimal;

/**
 * Response for retirement projection: current savings, projected value at retirement, and retirement age.
 */
public class RetirementProjectionResponse {

    private BigDecimal currentSavings;
    private BigDecimal projectedValue;
    private int retirementAge;

    public RetirementProjectionResponse() {
    }

    public RetirementProjectionResponse(BigDecimal currentSavings, BigDecimal projectedValue, int retirementAge) {
        this.currentSavings = currentSavings;
        this.projectedValue = projectedValue;
        this.retirementAge = retirementAge;
    }

    public BigDecimal getCurrentSavings() {
        return currentSavings;
    }

    public void setCurrentSavings(BigDecimal currentSavings) {
        this.currentSavings = currentSavings;
    }

    public BigDecimal getProjectedValue() {
        return projectedValue;
    }

    public void setProjectedValue(BigDecimal projectedValue) {
        this.projectedValue = projectedValue;
    }

    public int getRetirementAge() {
        return retirementAge;
    }

    public void setRetirementAge(int retirementAge) {
        this.retirementAge = retirementAge;
    }
}
