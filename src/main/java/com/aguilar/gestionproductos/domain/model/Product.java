package com.aguilar.gestionproductos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;

    public boolean hasLowStock() {
        return stock != null && stock < 5;
    }

    public boolean isValid() {
        return name != null && !name.isBlank()
                && price != null && price.compareTo(BigDecimal.ZERO) > 0
                && stock != null && stock >= 0;
    }

    public Product applyPriceConversion(BigDecimal exchangeRate) {
        return Product.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price.multiply(exchangeRate))
                .stock(this.stock)
                .build();
    }
}
