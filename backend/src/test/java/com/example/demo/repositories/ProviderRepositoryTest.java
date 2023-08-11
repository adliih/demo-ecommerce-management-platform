package com.example.demo.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.enums.ProviderName;

@SpringBootTest
public class ProviderRepositoryTest {

  @Autowired
  private ProviderRepository providerRepository;

  @Test
  public void findAll() {
    assertNotNull(providerRepository.findAll());
  }

  @Test
  public void findShopify() {
    assertNotNull(providerRepository.findOneByName(ProviderName.SHOPIFY.name));
  }
}
