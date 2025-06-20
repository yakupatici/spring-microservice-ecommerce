package com.code.springboot.springmicroserviceecommerce.service;

import com.code.springboot.springmicroserviceecommerce.model.Product;
import java.util.List;

public interface IExternalProductService {
    List<Product> fetchProductsFromExternalApi();
} 