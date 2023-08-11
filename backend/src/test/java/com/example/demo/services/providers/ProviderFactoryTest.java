package com.example.demo.services.providers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.enums.ProviderName;
import com.example.demo.services.providers.shopify.ShopifyService;

@SpringBootTest
public class ProviderFactoryTest {

  @Autowired
  private ShopifyService shopifyService;

  @Autowired
  private ProviderFactory providerFactory;

  @Test
  void testGetShopify() throws Exception {
    assertEquals(shopifyService, providerFactory.getProvider(ProviderName.SHOPIFY));
    assertEquals(shopifyService, providerFactory.getProvider(ProviderName.SHOPIFY.name));
  }

  @Test
  void testUnregisteredProviderName() throws Exception {
    assertThrows(Exception.class, () -> {
      providerFactory.getProvider("Unknown");
    });
  }
}
