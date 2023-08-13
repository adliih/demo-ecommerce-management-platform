package com.example.demo.repositories;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Shop;

@Repository
public class CustomShopRepositoryImpl implements CustomShopRepository {
  private JdbcTemplate jdbcTemplate;

  public CustomShopRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void insertOnConflictDoUpdate(List<Shop> shops) {
    jdbcTemplate.batchUpdate("""
        INSERT INTO shop
          (
            id,
            unique_key,
            provider_shop_id,
            name,
            provider_id
          )
        VALUES
          ( ?, ?, ?, ?, ? )
        ON CONFLICT (unique_key)
        DO UPDATE SET
          provider_shop_id = EXCLUDED.provider_shop_id,
          name = EXCLUDED.name,
          provider_id = EXCLUDED.provider_id
        """,
        shops,
        100,
        (PreparedStatement ps, Shop shop) -> {
          ps.setObject(1, shop.getId());
          ps.setString(2, shop.getUniqueKey());
          ps.setString(3, shop.getProviderShopId());
          ps.setString(4, shop.getName());
          ps.setObject(5, shop.getProvider().getId());
        });
  }

}