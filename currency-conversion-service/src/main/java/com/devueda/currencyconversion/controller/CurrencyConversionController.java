package com.devueda.currencyconversion.controller;

import com.devueda.currencyconversion.proxy.CurrencyExchangeProxy;
import com.devueda.currencyconversion.model.CurrencyConversion;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    private Environment environment;

    private CurrencyExchangeProxy currencyExchangeProxy;

    public CurrencyConversionController(Environment environment, CurrencyExchangeProxy currencyExchangeProxy) {
        this.environment = environment;
        this.currencyExchangeProxy = currencyExchangeProxy;
    }

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversion> currencyConversion = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversion.class, uriVariables);
        return CurrencyConversion.builder()
                .id(100L)
                .quantity(quantity)
                .from(from)
                .to(to)
                .conversionMultiple(currencyConversion.getBody().getConversionMultiple())
                .totalCalculatedAmount(quantity.multiply(currencyConversion.getBody().getConversionMultiple()))
                .environment(environment.getProperty("local.server.port"))
                .build();
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveExchangeValue(from, to);
        return CurrencyConversion.builder()
                .id(100L)
                .quantity(quantity)
                .from(from)
                .to(to)
                .conversionMultiple(currencyConversion.getConversionMultiple())
                .totalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()))
                .environment(currencyConversion.getEnvironment() + " feign")
                .build();
    }
}
