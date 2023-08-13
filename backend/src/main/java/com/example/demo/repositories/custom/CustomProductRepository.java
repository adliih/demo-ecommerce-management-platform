package com.example.demo.repositories.custom;

import java.util.List;

import com.example.demo.entities.Product;

public interface CustomProductRepository {
  public void insertOnConflictDoUpdate(List<Product> products);
}
