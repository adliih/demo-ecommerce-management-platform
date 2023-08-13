package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Variant {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(unique = true)
  private String uniqueKey;
  private String providerVariantId;

  private String title;
  private Double price;
  private String sku;

  @Column(name = "product_id")
  private UUID productId;

  @ManyToOne
  @JoinColumn(name = "product_id", insertable = false, updatable = false)
  private Product product;

}
