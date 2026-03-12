package com.financial.platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Customer entity representing an end-user of the retirement platform.
 * Holds profile and retirement goal information.
 */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(nullable = false)
    private String email;

    /** Target retirement savings amount. */
    @Column(name = "retirement_goal", precision = 19, scale = 2)
    private BigDecimal retirementGoal;

    /** Risk tolerance: e.g. CONSERVATIVE, MODERATE, AGGRESSIVE */
    @Size(max = 50)
    @Column(name = "risk_profile")
    private String riskProfile;

    public Customer() {
    }

    public Customer(String name, String email, BigDecimal retirementGoal, String riskProfile) {
        this.name = name;
        this.email = email;
        this.retirementGoal = retirementGoal;
        this.riskProfile = riskProfile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getRetirementGoal() {
        return retirementGoal;
    }

    public void setRetirementGoal(BigDecimal retirementGoal) {
        this.retirementGoal = retirementGoal;
    }

    public String getRiskProfile() {
        return riskProfile;
    }

    public void setRiskProfile(String riskProfile) {
        this.riskProfile = riskProfile;
    }
}
