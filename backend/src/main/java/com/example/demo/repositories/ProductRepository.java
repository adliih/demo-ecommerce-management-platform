package com.example.demo.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Product;
import com.example.demo.repositories.custom.CustomProductRepository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, CustomProductRepository {
  @EntityGraph(attributePaths = { "variants" })
  List<Product> findWithVariantsByShopProviderId(UUID uuid);
}
