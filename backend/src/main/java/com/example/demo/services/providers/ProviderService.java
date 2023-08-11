package com.example.demo.services.providers;

import java.util.List;

import com.example.demo.entities.Product;

public interface ProviderService {
  public List<Product> products() throws Exception;
}
