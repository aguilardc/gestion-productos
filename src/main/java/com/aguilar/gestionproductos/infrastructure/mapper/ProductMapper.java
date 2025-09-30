package com.aguilar.gestionproductos.infrastructure.mapper;

import com.aguilar.gestionproductos.domain.model.Product;
import com.aguilar.gestionproductos.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toDomain(ProductEntity entity) {
        if (entity == null) return null;
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .build();
    }

    public ProductEntity toEntity(Product domain) {
        if (domain == null) return null;
        return ProductEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .price(domain.getPrice())
                .stock(domain.getStock())
                .build();
    }
}
