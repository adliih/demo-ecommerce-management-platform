package com.example.demo.repositories;

import java.util.List;

import com.example.demo.entities.Shop;

public interface CustomShopRepository {
  void insertOnConflictDoUpdate(List<Shop> shops);
}
