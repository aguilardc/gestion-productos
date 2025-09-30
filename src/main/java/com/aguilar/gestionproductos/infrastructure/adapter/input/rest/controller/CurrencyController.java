package com.aguilar.gestionproductos.infrastructure.adapter.input.rest.controller;

import com.aguilar.gestionproductos.application.service.CurrencyConversionService;
import com.aguilar.gestionproductos.infrastructure.adapter.input.rest.dto.ProductDTO;
import com.aguilar.gestionproductos.infrastructure.adapter.input.rest.mapper.ProductDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyConversionService conversionService;
    private final ProductDTOMapper mapper;

    @GetMapping("/products")
    public Flux<ProductDTO> getProductsInCurrency(
            @RequestParam(defaultValue = "USD") String from,
            @RequestParam String to) {
        log.info("REST request to convert products from {} to {}", from, to);
        return conversionService.getProductsInCurrency(from, to)
                .map(mapper::toDTO);
    }
}
