package com.aguilar.gestionproductos.domain.port.output;

import com.aguilar.gestionproductos.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPort {
    Mono<Product> save(Product product);

    Mono<Product> findById(Long id);

    Flux<Product> findAll();

    Flux<Product> findByStockLessThan(Integer threshold);

    Mono<Product> update(Product product);

    Mono<Void> deleteById(Long id);

    Mono<Boolean> existsById(Long id);

}
