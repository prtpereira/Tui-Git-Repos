package com.tui.gitmanager.service;

import com.bvgroup.exchangerates.exceptions.CustomRateException;
import com.bvgroup.exchangerates.models.Rate;
import com.bvgroup.exchangerates.repositories.RateRepository;
import com.bvgroup.exchangerates.service.ExternalRatesService;
import com.bvgroup.exchangerates.service.RatesService;
import com.bvgroup.exchangerates.util.ExchangeRateConverter;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashMap;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RatesServiceTest {

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private RatesService ratesService;

    private final ExternalRatesService externalRatesServiceMock = Mockito.mock(ExternalRatesService.class);

    @Test
    public void exchangeRateIsSaved() throws Exception, CustomRateException {
        final String from = "EUR";
        final String to = "GBP";

        BigDecimal exchangeRate = ratesService.getExchangeRate(from, to);
        Rate EurGbpRate = rateRepository.findByFromCurrencyAndToCurrency(from, to);

        Assertions.assertEquals(exchangeRate, ExchangeRateConverter.decode(EurGbpRate.getExchangeRate()));
    }

    @Test
    public void exchangeRateIsNotUpdatedBefore60Secs() throws Exception, CustomRateException {
        final String from = "GBP";
        final String to = "EUR";

        ratesService.getExchangeRate(from, to);

        Mockito.verify(externalRatesServiceMock, Mockito.times(0)).getExchangeRate(Mockito.any(), Mockito.any());
    }

    @Test
    public void getExchangeRates() throws Exception, CustomRateException {
        final String from = "USD";
        HashMap<String, BigDecimal> exchangeRates = ratesService.getExchangeRates(from);

        Assertions.assertFalse(exchangeRates.isEmpty());
    }

    @Test
    public void convertAmountReturns() throws Exception, CustomRateException {
        final String from = "EUR";
        final String to = "USD";
        final BigDecimal amount = BigDecimal.valueOf(150);

        BigDecimal exchangeRate = ratesService.getExchangeRate(from, to);
        BigDecimal convertedAmount = ratesService.convertAmount(from, to, amount);

        Assertions.assertEquals(convertedAmount, amount.multiply(exchangeRate));
    }
}