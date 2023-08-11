package com.example.demo.enums;

public enum ProviderName {
  SHOPIFY("Shopify");

  private final String value;

  ProviderName(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
