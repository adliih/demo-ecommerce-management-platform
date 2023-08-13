package com.example.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, UUID>, CustomShopRepository {
  Shop findOneByProviderId(UUID uuid);
}
