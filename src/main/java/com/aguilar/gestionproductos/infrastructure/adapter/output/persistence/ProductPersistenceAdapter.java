package com.aguilar.gestionproductos.infrastructure.adapter.output.persistence;

import com.aguilar.gestionproductos.domain.model.Product;
import com.aguilar.gestionproductos.domain.port.output.ProductPort;
import com.aguilar.gestionproductos.infrastructure.adapter.output.persistence.repository.ProductRepository;
import com.aguilar.gestionproductos.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductPort {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public Mono<Product> save(Product product) {
        return Mono.just(product)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findAll() {
        return repository.findAll().map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findByStockLessThan(Integer threshold) {
        return repository.findByStockLessThan(threshold).map(mapper::toDomain);
    }

    @Override
    public Mono<Product> update(Product product) {
        return Mono.just(product)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return repository.existsById(id);
    }
}
