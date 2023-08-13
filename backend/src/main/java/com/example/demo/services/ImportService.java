package com.example.demo.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Provider;
import com.example.demo.entities.Shop;
import com.example.demo.entities.Variant;
import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.ProviderRepository;
import com.example.demo.repositories.ShopRepository;
import com.example.demo.repositories.VariantRepository;
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

  @Autowired
  private VariantRepository variantRepository;

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

      // Extract the variants
      List<Variant> existingVariants = existingProducts.stream()
          .flatMap(product -> product.getVariants().stream())
          .toList();
      List<Variant> externalVariants = updatedProducts.stream()
          .flatMap(product -> product.getVariants().stream())
          .toList();

      List<Variant> updatedVariants = transformVariants(existingVariants, externalVariants);

      // Persist the transformed data into your database
      shopRepository.insertOnConflictDoUpdate(Arrays.asList(updatedShop));
      productRepository.insertOnConflictDoUpdate(updatedProducts);
      variantRepository.insertOnConflictDoUpdate(updatedVariants);
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
        UUID productId = existingProduct.getId();
        updatedProductBuilder.id(productId);

        // readjust any of variant productId
        List<Variant> adjustedVariants = externalProduct.getVariants().stream().map(v -> {
          return v.toBuilder().productId(productId).build();
        }).toList();
        updatedProductBuilder.variants(adjustedVariants);
      }
      products.add(updatedProductBuilder.build());
    }

    return products;
  }

  private List<Variant> transformVariants(List<Variant> existingVariants, List<Variant> externalVariants) {
    Map<String, Variant> existingVariantMap = existingVariants.stream()
        .collect(Collectors.toMap(Variant::getUniqueKey, Function.identity()));

    List<Variant> transformedVariants = new ArrayList<>();

    for (Variant externalVariant : externalVariants) {
      Variant existingVariant = existingVariantMap.get(externalVariant.getUniqueKey());
      Variant.VariantBuilder transformedVariantBuilder = externalVariant
          .toBuilder();

      if (existingVariant != null) {
        // Assign existing variant id
        transformedVariantBuilder.id(existingVariant.getId());
      }

      transformedVariants.add(externalVariant);
    }

    return transformedVariants;
  }

}
