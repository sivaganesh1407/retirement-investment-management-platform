package com.financial.platform.service;

import com.financial.platform.model.Investment;
import com.financial.platform.model.dto.InvestmentRequest;
import com.financial.platform.repository.InvestmentRepository;
import com.financial.platform.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceTest {

    @Mock
    private InvestmentRepository investmentRepository;
    @Mock
    private PortfolioRepository portfolioRepository;

    private InvestmentService service;

    @BeforeEach
    void setUp() {
        service = new InvestmentService(investmentRepository, portfolioRepository);
    }

    @Test
    void addToPortfolio_throwsWhenPortfolioNotFound() {
        when(portfolioRepository.existsById(99L)).thenReturn(false);
        InvestmentRequest request = new InvestmentRequest();
        request.setAssetName("ETF");
        request.setAmount(BigDecimal.valueOf(10000));

        assertThatThrownBy(() -> service.addToPortfolio(99L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Portfolio not found");
    }

    @Test
    void addToPortfolio_savesInvestment() {
        when(portfolioRepository.existsById(1L)).thenReturn(true);
        InvestmentRequest request = new InvestmentRequest();
        request.setAssetName("S&P 500 ETF");
        request.setAssetType("ETF");
        request.setAmount(BigDecimal.valueOf(5000));
        request.setPurchaseDate(LocalDate.of(2024, 1, 15));

        Investment saved = new Investment(1L, "S&P 500 ETF", "ETF", BigDecimal.valueOf(5000), LocalDate.of(2024, 1, 15));
        saved.setId(10L);
        when(investmentRepository.save(any(Investment.class))).thenReturn(saved);

        Investment result = service.addToPortfolio(1L, request);

        assertThat(result.getId()).isEqualTo(10L);
        ArgumentCaptor<Investment> captor = ArgumentCaptor.forClass(Investment.class);
        verify(investmentRepository).save(captor.capture());
        assertThat(captor.getValue().getPortfolioId()).isEqualTo(1L);
        assertThat(captor.getValue().getAssetName()).isEqualTo("S&P 500 ETF");
        assertThat(captor.getValue().getAmount()).isEqualByComparingTo(BigDecimal.valueOf(5000));
    }

    @Test
    void findByPortfolioId_returnsFromRepository() {
        List<Investment> list = List.of(new Investment());
        when(investmentRepository.findByPortfolioId(1L)).thenReturn(list);
        assertThat(service.findByPortfolioId(1L)).isEqualTo(list);
    }
}
