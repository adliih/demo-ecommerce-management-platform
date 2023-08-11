package com.example.demo.enums;

import java.util.HashMap;
import java.util.Map;

public enum ProviderName {
  SHOPIFY("Shopify");

  private static final Map<String, ProviderName> BY_NAME = new HashMap<>();

  static {
    for (ProviderName e : values()) {
      BY_NAME.put(e.name, e);
    }
  }

  public final String name;

  ProviderName(String name) {
    this.name = name;
  }

  public static ProviderName valueOfName(String name) {
    return BY_NAME.get(name);
  }

}
