package com.example.demo.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Provider;
import com.example.demo.entities.Shop;
import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.ProviderRepository;
import com.example.demo.repositories.ShopRepository;
import com.example.demo.services.providers.ProviderFactory;
import com.example.demo.services.providers.ProviderService;

@Service
public class ImportService {

  @Autowired
  private ProviderFactory providerFactory;

  @Autowired
  private ProviderRepository providerRepository;

  @Autowired
  private ShopRepository shopRepository;

  @Autowired
  private ProductRepository productRepository;

  public void importProviderData() throws Exception {
    // Find all providers from the database
    List<Provider> providers = providerRepository.findAll();

    for (Provider provider : providers) {
      // Get the appropriate ProviderService using ProviderFactory
      ProviderService providerService = providerFactory.getProvider(provider.getName());

      // Fetch the existing shop and products from repositories
      Shop existingShop = shopRepository.findOneByProviderId(provider.getId());
      List<Product> existingProducts = productRepository.findWithVariantsByShopProviderId(provider.getId());

      // fetch the shop and products from provider service
      Shop shop = providerService.shop();
      List<Product> products = providerService.products();

      // Transform / create the updated entity based on existing and external entity
      Shop updatedShop = transformShop(provider, existingShop, shop);
      List<Product> updatedProducts = transformProducts(updatedShop, existingProducts, products);

      // Persist the transformed data into your database
      shopRepository.insertOnConflictDoUpdate(Arrays.asList(updatedShop));
      productRepository.insertOnConflictDoUpdate(updatedProducts);
    }
  }

  private Shop transformShop(Provider provider, Shop existingShop, Shop externalShop) {
    Shop.ShopBuilder builder = externalShop
        .toBuilder()
        .provider(provider);
    if (existingShop == null) {
      return builder.build();
    }

    return builder
        .id(existingShop.getId())
        .build();
  }

  private List<Product> transformProducts(Shop shop, List<Product> existingProducts, List<Product> externalProducts) {
    List<Product> products = new ArrayList<>();

    Map<String, Product> existingProductMap = new HashMap<>();
    for (Product existingProduct : existingProducts) {
      existingProductMap.put(existingProduct.getUniqueKey(), existingProduct);
    }

    for (Product externalProduct : externalProducts) {
      Product.ProductBuilder updatedProductBuilder = externalProduct.toBuilder()
          .shop(shop);

      Product existingProduct = existingProductMap.get(externalProduct.getUniqueKey());
      if (existingProduct != null) {
        updatedProductBuilder.id(existingProduct.getId());
      }
      products.add(updatedProductBuilder.build());
    }

    return products;
  }
}
