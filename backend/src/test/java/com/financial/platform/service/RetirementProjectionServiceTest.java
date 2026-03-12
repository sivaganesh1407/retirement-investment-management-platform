package com.financial.platform.service;

import com.financial.platform.model.Customer;
import com.financial.platform.model.Portfolio;
import com.financial.platform.model.dto.RetirementProjectionResponse;
import com.financial.platform.repository.CustomerRepository;
import com.financial.platform.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetirementProjectionServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PortfolioRepository portfolioRepository;

    private RetirementProjectionService service;

    @BeforeEach
    void setUp() {
        service = new RetirementProjectionService(customerRepository, portfolioRepository);
    }

    @Test
    void getProjection_throwsWhenCustomerNotFound() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getProjection(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Customer not found");
    }

    @Test
    void getProjection_returnsResponseWithZeroSavingsWhenNoPortfolios() {
        Customer customer = new Customer("Jane", "jane@example.com", BigDecimal.valueOf(500000), "MODERATE");
        customer.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(portfolioRepository.findByCustomerIdWithInvestments(1L)).thenReturn(Collections.emptyList());

        RetirementProjectionResponse response = service.getProjection(1L);

        assertThat(response).isNotNull();
        assertThat(response.getCurrentSavings()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.getProjectedValue()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        assertThat(response.getRetirementAge()).isEqualTo(65);
    }
}
