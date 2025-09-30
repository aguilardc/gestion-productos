package com.aguilar.gestionproductos.infrastructure.adapter.input.rest.mapper;

import com.aguilar.gestionproductos.domain.model.Product;
import com.aguilar.gestionproductos.infrastructure.adapter.input.rest.dto.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOMapper {
    public ProductDTO toDTO(Product domain) {
        if (domain == null) return null;
        return ProductDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .price(domain.getPrice())
                .stock(domain.getStock())
                .build();
    }

    public Product toDomain(ProductDTO dto) {
        if (dto == null) return null;
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .build();
    }
}
