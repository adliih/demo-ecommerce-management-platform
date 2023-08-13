package com.example.demo.services.providers.shopify;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Product;
import com.example.demo.entities.Shop;
import com.example.demo.entities.Variant;
import com.example.demo.services.providers.ProviderService;
import com.example.demo.services.providers.shopify.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class ShopifyService implements ProviderService {

    private Logger logger = LoggerFactory.getLogger(ShopifyService.class);

    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private ObjectMapper objectMapper;

    private String HEADER_ACCESS_TOKEN_KEY = "X-Shopify-Access-Token";
    private String UNIQUE_KEY_PREFIX = "Shopify-";
    private String API_VERSION = "2023-07";

    private String graphqlQueryShop = """
            {
                shop {
                    name
                    id
                }
            }
            """;

    @Value("${shopify.api.shop-domain}")
    private String shopDomain;

    @Value("${shopify.api.admin-access-token}")
    private String adminAccessToken;

    private String getApiUrl(String path) {
        return new StringBuilder()
                .append("https://")
                .append(shopDomain)
                .append(".myshopify.com/admin/api/")
                .append(API_VERSION)
                .append("/")
                .append(path)
                .toString();
    }

    @Override
    public List<Product> products() throws Exception {
        HttpGet httpGet = new HttpGet(getApiUrl("products.json"));
        JsonNode productsTree = executeApi(httpGet).get("products");

        ShopifyProduct[] products = objectMapper.convertValue(productsTree, ShopifyProduct[].class);
        // map products into product entity
        return Arrays.stream(products).map(this::toProduct).toList();
    }

    @Override
    public Shop shop() throws Exception {
        JsonNode shopNode = graphql(graphqlQueryShop).get("data").get("shop");
        ShopifyShop shop = objectMapper.convertValue(shopNode, ShopifyShop.class);

        return this.toShop(shop);
    }

    private Shop toShop(ShopifyShop shop) {
        return Shop.builder()
                .id(UUID.randomUUID())
                .uniqueKey(UNIQUE_KEY_PREFIX + shop.id)
                .providerShopId(shop.id)
                .name(shop.name)
                .build();
    }

    private JsonNode graphql(String query) throws Exception {
        query = query.replaceAll("\\s+", " ");
        logger.info("Calling GraphQL: " + query);

        HttpPost request = new HttpPost(getApiUrl("graphql.json"));
        request.setHeader(HEADER_ACCESS_TOKEN_KEY, adminAccessToken);

        // Set the query into the request body
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("query", query);
        request.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logger.info("Success called GraphQL: {} Result: {}", query, responseBody);
            return objectMapper.readTree(responseBody);
        } catch (Exception e) {
            logger.error("Failed to call API: " + query, e);
            throw e;
        }
    }

    private JsonNode executeApi(HttpUriRequest request) throws Exception {
        String url = request.getURI().toString();
        logger.info("Calling API: " + url);
        request.setHeader(HEADER_ACCESS_TOKEN_KEY, adminAccessToken);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logger.info("Success called API: {}. Result: {}", url, responseBody);
            return objectMapper.readTree(responseBody);
        } catch (Exception e) {
            logger.error("Failed to call API: " + url, e);
            throw e;
        }
    }

    private Product toProduct(ShopifyProduct p) {
        return Product.builder()
                .id(UUID.randomUUID())
                .providerProductId(p.id.toString())
                .uniqueKey(UNIQUE_KEY_PREFIX + p.id)
                .title(p.title)
                .variants(Arrays.stream(p.variants).map(this::toVariant).toList())
                .build();
    }

    private Variant toVariant(ShopifyVariant variant) {
        return Variant.builder()
                .id(UUID.randomUUID())
                .providerVariantId(variant.id.toString())
                .uniqueKey(UNIQUE_KEY_PREFIX + variant.id)
                .price(variant.price.doubleValue())
                .sku(variant.sku)
                .title(variant.title)
                .build();
    }
}