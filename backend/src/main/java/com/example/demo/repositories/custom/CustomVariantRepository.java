package com.example.demo.repositories.custom;

import com.example.demo.entities.Variant;

import java.util.List;

public interface CustomVariantRepository {
  void insertOnConflictDoUpdate(List<Variant> variants);
}
