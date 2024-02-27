package com.bvgroup.exchangerates.acceptance.controllers;

import com.bvgroup.exchangerates.controllers.RatesController;
import com.bvgroup.exchangerates.exceptions.CustomRateException;
import com.bvgroup.exchangerates.service.RatesService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RatesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final RatesController ratesController;

    @MockBean
    private RatesService ratesService;

    public RatesControllerTest() {
        ratesService = Mockito.mock(RatesService.class);
        ratesController = new RatesController(ratesService);
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ratesController).build();
    }

    private final static String GET_EXCHANGE_RATE = "/api/rate";
    private final static String GET_EXCHANGE_RATES = "/api/rates";
    private final static String GET_CONVERT = "/api/convert";

    @Test
    public void getRateToCurrency() throws Exception, CustomRateException {
        final String fromCurrency = "EUR";
        final String toCurrency = "GBP";
        final BigDecimal exchangeRate = BigDecimal.valueOf(0.86826);
        final String GET_EXCHANGE_RATE_EUR_TO_GBP = GET_EXCHANGE_RATE + "?from=" + fromCurrency + "&to=" + toCurrency;

        Mockito.when(ratesService.getExchangeRate(fromCurrency, toCurrency)).thenReturn(exchangeRate);

        mockMvc.perform(
            MockMvcRequestBuilders
                .get(GET_EXCHANGE_RATE_EUR_TO_GBP)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.from", Matchers.is("EUR")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.to", Matchers.is("GBP")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exchange_rate", Matchers.is(0.86826)));
    }

    @Test
    public void getRatesFromCurrency() throws Exception, CustomRateException {
        final String fromCurrency = "EUR";
        final String GET_EXCHANGE_RATES_USD = GET_EXCHANGE_RATES + "?from=" + fromCurrency;
        final HashMap<String, BigDecimal> exchangeRates = new HashMap<>(Map.of(
                "LAK", BigDecimal.valueOf(21758.945682),
                "USD", BigDecimal.valueOf(1.053402),
                "CAD", BigDecimal.valueOf(1.441992),
                "BRL", BigDecimal.valueOf(5.334116)
        ));

        Mockito.when(ratesService.getExchangeRates(fromCurrency)).thenReturn(exchangeRates);

        mockMvc.perform(
            MockMvcRequestBuilders
                .get(GET_EXCHANGE_RATES_USD)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.from", Matchers.is("EUR")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exchange_rates", Matchers.aMapWithSize(4)));
    }

    @Test
    public void getRatesFromInvalidCurrency() throws Exception, CustomRateException {
        final String GET_EXCHANGE_RATES_USD = GET_EXCHANGE_RATES;

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(GET_EXCHANGE_RATES_USD)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void convertAmount() throws Exception, CustomRateException {
        final BigDecimal amount = BigDecimal.valueOf(130.55);
        final String fromCurrency = "EUR";
        final List<String> toCurrencies = List.of("GBP","LAK","CAD","USD");
        final String GET_CONVERT_AMOUNT = GET_CONVERT + "?from=" + fromCurrency + "&to=" + toCurrencies + "&amount=" + amount;

        final HashMap<String, BigDecimal> convertedAmounts = new HashMap<>(Map.of(
                "GBP", BigDecimal.valueOf(113.70905),
                "LAK", BigDecimal.valueOf(2854524.13169945),
                "CAD", BigDecimal.valueOf(189.13248480),
                "USD", BigDecimal.valueOf(137.84761445)
        ));

        Mockito.when(ratesService.convertAmount(Mockito.eq(fromCurrency), Mockito.anyList(), Mockito.eq(amount))).thenReturn(convertedAmounts);

        mockMvc.perform(
            MockMvcRequestBuilders
                .get(GET_CONVERT_AMOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.amount", Matchers.is(130.55)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.from", Matchers.is("EUR")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.converted_amounts", Matchers.aMapWithSize(4)));
    }
}
