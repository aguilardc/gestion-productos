package com.aguilar.gestionproductos.infrastructure.adapter.input.rest.controller;

import com.aguilar.gestionproductos.application.service.ProductService;
import com.aguilar.gestionproductos.infrastructure.adapter.input.rest.dto.ProductDTO;
import com.aguilar.gestionproductos.infrastructure.adapter.input.rest.mapper.ProductDTOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductDTOMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductDTO> createProduct(@Valid @RequestBody ProductDTO dto) {
        log.info("REST request to create product: {}", dto.getName());
        return Mono.just(dto)
                .map(mapper::toDomain)
                .flatMap(productService::createProduct)
                .map(mapper::toDTO);
    }

    @GetMapping("/{id}")
    public Mono<ProductDTO> getProduct(@PathVariable Long id) {
        log.info("REST request to get product: {}", id);
        return productService.getProductById(id)
                .map(mapper::toDTO);
    }

    @GetMapping
    public Flux<ProductDTO> getAllProducts() {
        log.info("REST request to get all products");
        return productService.getAllProducts()
                .map(mapper::toDTO);
    }

    @GetMapping("/low-stock")
    public Flux<ProductDTO> getLowStockProducts() {
        log.info("REST request to get low stock products");
        return productService.getLowStockProducts()
                .map(mapper::toDTO);
    }

    @PutMapping("/{id}")
    public Mono<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO dto) {
        log.info("REST request to update product: {}", id);
        return Mono.just(dto)
                .map(mapper::toDomain)
                .flatMap(product -> productService.updateProduct(id, product))
                .map(mapper::toDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(@PathVariable Long id) {
        log.info("REST request to delete product: {}", id);
        return productService.deleteProduct(id);
    }
}
