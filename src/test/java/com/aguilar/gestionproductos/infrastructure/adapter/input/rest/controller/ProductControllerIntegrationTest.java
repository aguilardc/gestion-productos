package com.aguilar.gestionproductos.infrastructure.adapter.input.rest.controller;


import com.aguilar.gestionproductos.infrastructure.adapter.input.rest.dto.ProductDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ProductControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Should create product via REST API")
    void shouldCreateProduct() {
        ProductDTO newProduct = ProductDTO.builder()
                .name("Integration Test Product")
                .price(new BigDecimal("299.99"))
                .stock(50)
                .build();

        webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newProduct)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Integration Test Product")
                .jsonPath("$.price").isEqualTo(299.99)
                .jsonPath("$.stock").isEqualTo(50);
    }

    @Test
    @DisplayName("Should get all products")
    void shouldGetAllProducts() {
        webTestClient.get()
                .uri("/api/products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductDTO.class)
                .hasSize(9);
    }

    @Test
    @DisplayName("Should get low stock products")
    void shouldGetLowStockProducts() {
        webTestClient.get()
                .uri("/api/products/low-stock")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductDTO.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    response.getResponseBody().forEach(product ->
                            assertTrue(product.getStock() < 5, "Product " + product.getName() + " should have stock < 5")
                    );
                });
    }

    @Test
    @DisplayName("Should return 404 when product not found")
    void shouldReturn404WhenProductNotFound() {
        webTestClient.get()
                .uri("/api/products/9999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should return 400 when validation fails")
    void shouldReturn400WhenValidationFails() {
        ProductDTO invalidProduct = ProductDTO.builder()
                .name("AB")
                .price(new BigDecimal("-10"))
                .stock(-5)
                .build();

        webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidProduct)
                .exchange()
                .expectStatus().isBadRequest();
    }
}