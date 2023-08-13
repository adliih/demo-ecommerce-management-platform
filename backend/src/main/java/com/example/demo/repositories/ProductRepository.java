package com.example.demo.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, CustomProductRepository {
  List<Product> findWithVariantsByShopProviderId(UUID uuid);
}
