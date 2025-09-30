package com.aguilar.gestionproductos.application.service;

import com.aguilar.gestionproductos.domain.exception.InvalidProductException;
import com.aguilar.gestionproductos.domain.exception.ProductNotFoundException;
import com.aguilar.gestionproductos.domain.model.Product;
import com.aguilar.gestionproductos.domain.port.output.ProductPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductPort productPort;

    @InjectMocks
    private ProductService productService;

    private Product validProduct;

    @BeforeEach
    void setUp() {
        validProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .stock(10)
                .build();
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProductSuccessfully() {
        when(productPort.save(any(Product.class))).thenReturn(Mono.just(validProduct));

        StepVerifier.create(productService.createProduct(validProduct))
                .expectNext(validProduct)
                .verifyComplete();

        verify(productPort, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw exception when creating invalid product")
    void shouldThrowExceptionWhenCreatingInvalidProduct() {
        Product invalidProduct = Product.builder()
                .name("")
                .price(new BigDecimal("-10"))
                .stock(-5)
                .build();

        StepVerifier.create(productService.createProduct(invalidProduct))
                .expectError(InvalidProductException.class)
                .verify();

        verify(productPort, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should get product by id successfully")
    void shouldGetProductByIdSuccessfully() {
        when(productPort.findById(1L)).thenReturn(Mono.just(validProduct));

        StepVerifier.create(productService.getProductById(1L))
                .expectNext(validProduct)
                .verifyComplete();

        verify(productPort, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when product not found")
    void shouldThrowExceptionWhenProductNotFound() {
        when(productPort.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(productService.getProductById(999L))
                .expectError(ProductNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should get all products")
    void shouldGetAllProducts() {
        Product product2 = Product.builder()
                .id(2L)
                .name("Product 2")
                .price(new BigDecimal("149.99"))
                .stock(5)
                .build();

        when(productPort.findAll()).thenReturn(Flux.just(validProduct, product2));

        StepVerifier.create(productService.getAllProducts())
                .expectNext(validProduct)
                .expectNext(product2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get low stock products")
    void shouldGetLowStockProducts() {
        Product lowStockProduct = Product.builder()
                .id(3L)
                .name("Low Stock Product")
                .price(new BigDecimal("49.99"))
                .stock(2)
                .build();

        when(productPort.findByStockLessThan(5)).thenReturn(Flux.just(lowStockProduct));

        StepVerifier.create(productService.getLowStockProducts())
                .expectNext(lowStockProduct)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProductSuccessfully() {
        Product updatedProduct = Product.builder()
                .id(1L)
                .name("Updated Product")
                .price(new BigDecimal("119.99"))
                .stock(15)
                .build();

        when(productPort.existsById(1L)).thenReturn(Mono.just(true));
        when(productPort.update(any(Product.class))).thenReturn(Mono.just(updatedProduct));

        StepVerifier.create(productService.updateProduct(1L, updatedProduct))
                .expectNext(updatedProduct)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent product")
    void shouldThrowExceptionWhenUpdatingNonExistentProduct() {
        when(productPort.existsById(anyLong())).thenReturn(Mono.just(false));

        StepVerifier.create(productService.updateProduct(999L, validProduct))
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productPort, never()).update(any(Product.class));
    }

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProductSuccessfully() {
        when(productPort.existsById(1L)).thenReturn(Mono.just(true));
        when(productPort.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productService.deleteProduct(1L))
                .verifyComplete();

        verify(productPort, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent product")
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        when(productPort.existsById(anyLong())).thenReturn(Mono.just(false));

        StepVerifier.create(productService.deleteProduct(999L))
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productPort, never()).deleteById(anyLong());
    }
}