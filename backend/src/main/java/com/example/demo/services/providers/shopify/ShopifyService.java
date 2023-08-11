package com.example.demo.services.providers.shopify;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.services.providers.shopify.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ShopifyService {

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    private String HEADER_ACCESS_TOKEN_KEY = "X-Shopify-Access-Token";

    public ShopifyService(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = (CloseableHttpClient) httpClient;
        this.objectMapper = objectMapper;
    }

    @Value("${shopify.api.shop-domain}")
    private String shopDomain;

    @Value("${shopify.api.admin-access-token}")
    private String adminAccessToken;

    public List<Product> products() throws Exception {
        HttpGet httpGet = new HttpGet("https://" + shopDomain + ".myshopify.com/admin/api/2023-07/products.json");
        httpGet.setHeader(HEADER_ACCESS_TOKEN_KEY, adminAccessToken);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonNode productsTree = objectMapper.readTree(responseBody).get("products");
            return objectMapper.convertValue(productsTree, new TypeReference<List<Product>>() {
            });
        } catch (Exception e) {
            throw e;
        }
    }
}