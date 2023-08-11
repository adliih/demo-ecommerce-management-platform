package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "providerId", "providerShopId" }))
public class Shop {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "provider_id")
  private Provider provider;

  private String providerShopId;

  @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Product> products;
}
