package com.itgenius.ordermgtsystem.service;

import com.itgenius.ordermgtsystem.dto.request.CreateProductRequest;
import com.itgenius.ordermgtsystem.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse getProduct(Long productId);

    List<ProductResponse> getAllProducts();

    ProductResponse updateProduct(Long productId, CreateProductRequest request);

    void deleteProduct(Long productId);
}
