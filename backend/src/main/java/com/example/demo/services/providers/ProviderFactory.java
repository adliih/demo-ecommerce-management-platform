package com.example.demo.services.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.enums.ProviderName;
import com.example.demo.services.providers.shopify.ShopifyService;

@Service
public class ProviderFactory {

  @Autowired
  private ShopifyService shopifyService;

  public ProviderService getProvider(ProviderName providerName) throws Exception {
    switch (providerName) {
      case SHOPIFY:
        return shopifyService;
      default:
        throw new Exception("Unregistered provider: " + providerName);
    }
  }

  public ProviderService getProvider(String name) throws Exception {
    ProviderName providerName = ProviderName.valueOfName(name);
    return getProvider(providerName);

  }
}