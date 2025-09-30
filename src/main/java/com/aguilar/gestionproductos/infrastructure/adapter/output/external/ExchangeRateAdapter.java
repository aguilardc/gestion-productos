package com.aguilar.gestionproductos.infrastructure.adapter.output.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.aguilar.gestionproductos.domain.exception.ExternalServiceException;
import com.aguilar.gestionproductos.domain.port.output.ExchangeRatePort;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateAdapter implements ExchangeRatePort {

    private final WebClient.Builder webClientBuilder;

    @Value("${external.api.exchange-rate.base-url}")
    private String baseUrl;

    @Value("${external.api.exchange-rate.timeout}")
    private int timeout;

    @Override
    public Mono<BigDecimal> getExchangeRate(String fromCurrency, String toCurrency) {
        log.debug("Fetching exchange rate: {} -> {}", fromCurrency, toCurrency);
        return webClientBuilder.baseUrl(baseUrl).build()
                .get()
                .uri("/{currency}", fromCurrency)
                .retrieve()
                .bodyToMono(ExchangeRateResponse.class)
                .timeout(Duration.ofMillis(timeout))
                .map(response -> extractRate(response, toCurrency))
                .doOnSuccess(rate -> log.info("Exchange rate obtained: {}", rate))
                .onErrorMap(ex -> new ExternalServiceException("Failed to fetch exchange rate", ex));
    }

    private BigDecimal extractRate(ExchangeRateResponse response, String currency) {
        BigDecimal rate = response.getRates().get(currency);
        if (rate == null) {
            throw new ExternalServiceException("Currency not found: " + currency, null);
        }
        return rate;
    }

    @Data
    private static class ExchangeRateResponse {
        @JsonProperty("base")
        private String baseCurrency;

        @JsonProperty("rates")
        private Map<String, BigDecimal> rates;
    }
}
