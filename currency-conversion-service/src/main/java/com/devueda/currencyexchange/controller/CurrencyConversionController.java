package com.devueda.currencyexchange.controller;

import com.devueda.currencyexchange.model.CurrencyConversion;
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

    public CurrencyConversionController(Environment environment) {
        this.environment = environment;
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
}
