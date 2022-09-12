package com.devueda.currencyexchange.controller;

import com.devueda.currencyexchange.model.CurrencyExchange;
import com.devueda.currencyexchange.repository.CurrencyExchangeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CurrencyExchangeController {

    private Environment environment;
    private CurrencyExchangeRepository repository;

    public CurrencyExchangeController(Environment environment, CurrencyExchangeRepository repository) {
        this.environment = environment;
        this.repository = repository;
    }

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        log.info("retrieveExchangeValue called with {} to {}", from, to);
        var currencyExchange = repository.findByFromAndTo(from, to);
        if(currencyExchange == null) {
            throw new RuntimeException(String.format("Unable to find data for %s and %s", from, to));
        }
        currencyExchange.setEnvironment(environment.getProperty("local.server.port"));
        return currencyExchange;
    }
}
