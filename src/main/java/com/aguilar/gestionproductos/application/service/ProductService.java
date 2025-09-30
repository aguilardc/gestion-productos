package com.aguilar.gestionproductos.application.service;

import com.aguilar.gestionproductos.domain.exception.InvalidProductException;
import com.aguilar.gestionproductos.domain.exception.ProductNotFoundException;
import com.aguilar.gestionproductos.domain.model.Product;
import com.aguilar.gestionproductos.domain.port.output.ProductPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private static final Integer LOW_STOCK_THRESHOLD = 5;
    private final ProductPort productPort;

    public Mono<Product> createProduct(Product product) {
        log.debug("Creating product: {}", product.getName());
        return Mono.just(product)
                .doOnNext(this::validateProduct)
                .flatMap(productPort::save)
                .doOnSuccess(saved -> log.info("Product created with id: {}", saved.getId()));
    }

    public Mono<Product> getProductById(Long id) {
        log.debug("Fetching product with id: {}", id);
        return productPort.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)));
    }

    public Flux<Product> getAllProducts() {
        log.debug("Fetching all products");
        return productPort.findAll();
    }

    public Flux<Product> getLowStockProducts() {
        log.debug("Fetching products with low stock (< {})", LOW_STOCK_THRESHOLD);
        return productPort.findByStockLessThan(LOW_STOCK_THRESHOLD);
    }

    public Mono<Product> updateProduct(Long id, Product product) {
        log.debug("Updating product with id: {}", id);
        return productPort.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new ProductNotFoundException(id));
                    }
                    this.validateProduct(product);
                    product.setId(id);
                    return productPort.update(product);
                })
                .doOnSuccess(updated -> log.info("Product updated: {}", id));
    }

    public Mono<Void> deleteProduct(Long id) {
        log.debug("Deleting product with id: {}", id);
        return productPort.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new ProductNotFoundException(id));
                    }
                    return productPort.deleteById(id);
                }).doOnSuccess(v -> log.info("Product deleted: {}", id));
    }

    private void validateProduct(Product product) {
        if (!product.isValid()) {
            throw new InvalidProductException(
                    "Product validation failed: name, price > 0, and stock >= 0 are required"
            );
        }
    }
}
