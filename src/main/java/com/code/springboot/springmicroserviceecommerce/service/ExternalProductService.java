package com.code.springboot.springmicroserviceecommerce.service;

import com.code.springboot.springmicroserviceecommerce.model.Product;
import com.code.springboot.springmicroserviceecommerce.exception.ExternalApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExternalProductService implements IExternalProductService {
    
    private final RestTemplate restTemplate;
    private final String fakeStoreApiUrl;
    private final ProductMapper productMapper;

    public ExternalProductService(@Value("${external.api.fakestore.url:https://fakestoreapi.com/products}") String fakeStoreApiUrl) {
        this.restTemplate = new RestTemplate();
        this.fakeStoreApiUrl = fakeStoreApiUrl;
        this.productMapper = new ProductMapper();
    }

    @Override
    public List<Product> fetchProductsFromExternalApi() {
        try {
            log.info("Fetching products from external API: {}", fakeStoreApiUrl);
            FakeStoreProduct[] fakeStoreProducts = restTemplate.getForObject(fakeStoreApiUrl, FakeStoreProduct[].class);
            
            if (fakeStoreProducts == null) {
                log.warn("No products returned from external API");
                return List.of();
            }

            return Arrays.stream(fakeStoreProducts)
                    .map(productMapper::convertToProduct)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching products from external API", e);
            throw new ExternalApiException("Failed to fetch products from external API", e);
        }
    }

    // Separate class for mapping (Single Responsibility Principle)
    private static class ProductMapper {
        public Product convertToProduct(FakeStoreProduct fakeStoreProduct) {
            Product product = new Product();
            product.setName(fakeStoreProduct.getTitle());
            product.setDescription(fakeStoreProduct.getDescription());
            product.setPrice(BigDecimal.valueOf(fakeStoreProduct.getPrice()));
            product.setImageUrl(fakeStoreProduct.getImage());
            product.setStockQuantity(20); // Default stock quantity
            return product;
        }
    }

    // DTO for Fake Store API response
    private static class FakeStoreProduct {
        private Long id;
        private String title;
        private Double price;
        private String description;
        private String image;
        private String category;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }
} 