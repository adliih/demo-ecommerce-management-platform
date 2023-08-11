package com.example.demo.services.providers.shopify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ShopifyVariant {
    public Long id;

    @JsonProperty("product_id")
    public Long productId;

    public String title;
    public BigDecimal price;
    public String sku;
    public Integer position;

    @JsonProperty("inventory_policy")
    public String inventoryPolicy;

    @JsonProperty("compare_at_price")
    public BigDecimal compareAtPrice;

    @JsonProperty("fulfillment_service")
    public String fulfillmentService;

    @JsonProperty("inventory_management")
    public String inventoryManagement;

    public String option1;
    public String option2;
    public String option3;

    @JsonProperty("created_at")
    public OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    public OffsetDateTime updatedAt;

    public boolean taxable;
    public String barcode;
    public int grams;

    @JsonProperty("image_id")
    public Long imageId;

    public int weight;

    @JsonProperty("weight_unit")
    public String weightUnit;

    @JsonProperty("inventory_item_id")
    public Long inventoryItemId;

    @JsonProperty("inventory_quantity")
    public int inventoryQuantity;

    @JsonProperty("old_inventory_quantity")
    public int oldInventoryQuantity;

    @JsonProperty("requires_shipping")
    public boolean requiresShipping;

    @JsonProperty("admin_graphql_api_id")
    public String adminGraphqlApiId;

    // Getters and setters
}
