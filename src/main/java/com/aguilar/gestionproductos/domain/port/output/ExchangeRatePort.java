package com.aguilar.gestionproductos.domain.port.output;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ExchangeRatePort {
    Mono<BigDecimal> getExchangeRate(String fromCurrency, String toCurrency);
}
