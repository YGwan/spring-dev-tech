package org.example.repository;

import org.example.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.example.domain.constant.ProductStatus.*;
import static org.example.domain.constant.ProductType.HANDMADE;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("test")
    @Test
    void test() {
        var product1 = Product.builder()
                .productNumber("001")
                .productType(HANDMADE)
                .productStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        var product2 = Product.builder()
                .productNumber("002")
                .productType(HANDMADE)
                .productStatus(HOLD)
                .name("카페라떼")
                .price(4500)
                .build();

        var product3 = Product.builder()
                .productNumber("003")
                .productType(HANDMADE)
                .productStatus(STOP)
                .name("팥빙수")
                .price(7000)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        var products = productRepository.findAllByProductStatusIn(List.of(SELLING, HOLD));

        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "productStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }
}