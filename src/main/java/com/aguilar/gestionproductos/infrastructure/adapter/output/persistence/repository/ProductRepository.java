package com.aguilar.gestionproductos.infrastructure.adapter.output.persistence.repository;

import com.aguilar.gestionproductos.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {

    @Query("SELECT * FROM products WHERE stock < :threshold")
    Flux<ProductEntity> findByStockLessThan(Integer threshold);
}
