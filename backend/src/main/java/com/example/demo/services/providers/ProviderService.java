package com.example.demo.services.providers;

import java.util.List;

import com.example.demo.entities.Product;
import com.example.demo.entities.Shop;

public interface ProviderService {
  public List<Product> products() throws Exception;

  public Shop shop() throws Exception;
}
