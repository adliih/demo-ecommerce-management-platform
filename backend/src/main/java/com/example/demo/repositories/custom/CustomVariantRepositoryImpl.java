package com.example.demo.repositories.custom;

import com.example.demo.entities.Variant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class CustomVariantRepositoryImpl implements CustomVariantRepository {

  private final JdbcTemplate jdbcTemplate;

  public CustomVariantRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void insertOnConflictDoUpdate(List<Variant> variants) {
    jdbcTemplate.batchUpdate("""
          INSERT INTO variant
            (id, unique_key, provider_variant_id, title, price, sku, product_id)
          VALUES
            (?, ?, ?, ?, ?, ?, ?)
          ON CONFLICT (unique_key)
          DO UPDATE SET
            provider_variant_id = EXCLUDED.provider_variant_id,
            title = EXCLUDED.title,
            price = EXCLUDED.price,
            sku = EXCLUDED.sku,
            product_id = EXCLUDED.product_id
        """,
        variants,
        100,
        (PreparedStatement ps, Variant variant) -> {
          ps.setObject(1, variant.getId());
          ps.setString(2, variant.getUniqueKey());
          ps.setString(3, variant.getProviderVariantId());
          ps.setString(4, variant.getTitle());
          ps.setDouble(5, variant.getPrice());
          ps.setString(6, variant.getSku());
          ps.setObject(7, variant.getProductId());
        });
  }
}
