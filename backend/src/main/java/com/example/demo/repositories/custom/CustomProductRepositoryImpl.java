package com.example.demo.repositories.custom;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Product;

@Repository
public class CustomProductRepositoryImpl implements CustomProductRepository {

  private JdbcTemplate jdbcTemplate;

  public CustomProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void insertOnConflictDoUpdate(List<Product> products) {
    jdbcTemplate.batchUpdate("""
        INSERT INTO product
          ( id, unique_key, provider_product_id, title, shop_id )
        VALUES
          ( ?, ?, ?, ?, ? )
        ON CONFLICT (unique_key)
        DO UPDATE SET
          provider_product_id = EXCLUDED.provider_product_id,
          title = EXCLUDED.title,
          shop_id = EXCLUDED.shop_id
        """,
        products,
        100,
        (PreparedStatement ps, Product product) -> {
          ps.setObject(1, product.getId());
          ps.setString(2, product.getUniqueKey());
          ps.setString(3, product.getProviderProductId());
          ps.setString(4, product.getTitle());
          ps.setObject(5, product.getShop().getId());
        });
  }

}
