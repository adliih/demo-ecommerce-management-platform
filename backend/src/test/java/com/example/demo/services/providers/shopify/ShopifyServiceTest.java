package com.example.demo.services.providers.shopify;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.entities.Product;

@SpringBootTest
class ShopifyServiceTest {

    @Autowired
    private ShopifyService shopifyService;

    @Test
    void testProducts() throws Exception {

        // Call the method
        List<Product> products = shopifyService.products();

        // Assert the result
        assertNotNull(products);
    }
}
