package com.financial.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Retirement Investment Management Platform.
 * Bootstraps the Spring Boot application and enables component scanning
 * for all packages under com.financial.platform.
 */
@SpringBootApplication
public class FinancialPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialPlatformApplication.class, args);
    }
}
