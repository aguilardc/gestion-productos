package com.aguilar.gestionproductos.application.service;

import com.aguilar.gestionproductos.domain.model.Product;
import com.aguilar.gestionproductos.domain.port.output.ExchangeRatePort;
import com.aguilar.gestionproductos.domain.port.output.ProductPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    private final ProductPort productPort;
    private final ExchangeRatePort exchangeRatePort;

    public Flux<Product> getProductsInCurrency(String fromCurrency, String toCurrency) {
        log.debug("Converting products from {} to {}", fromCurrency, toCurrency);
        return exchangeRatePort.getExchangeRate(fromCurrency, toCurrency)
                .flatMapMany(this::convertAllProducts)
                .doOnComplete(() -> log.info("Conversion completed"));
    }

    private Flux<Product> convertAllProducts(BigDecimal exchangeRate) {
        return productPort.findAll()
                .map(product -> product.applyPriceConversion(exchangeRate));
    }
}
