package com.itgenius.ordermgtsystem.service.impl;

import com.itgenius.ordermgtsystem.domain.Product;
import com.itgenius.ordermgtsystem.domain.Stock;
import com.itgenius.ordermgtsystem.dto.request.CreateProductRequest;
import com.itgenius.ordermgtsystem.dto.response.ProductResponse;
import com.itgenius.ordermgtsystem.exception.BusinessException;
import com.itgenius.ordermgtsystem.exception.ErrorCode;
import com.itgenius.ordermgtsystem.repository.ProductRepository;
import com.itgenius.ordermgtsystem.repository.StockRepository;
import com.itgenius.ordermgtsystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = Product.create(request.name(), request.description(), request.price());
        productRepository.save(product);

        Stock stock = Stock.create(product, request.initialStock());
        stockRepository.save(stock);

        return toResponse(product, stock);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long productId) {
        Product product = findProductById(productId);
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STOCK_NOT_FOUND));
        return toResponse(product, stock);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> {
                    Stock stock = stockRepository.findByProductId(product.getId())
                            .orElse(null);
                    return toResponse(product, stock);
                })
                .toList();
    }

    @Override
    public ProductResponse updateProduct(Long productId, CreateProductRequest request) {
        Product product = productRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        product.update(request.name(), request.description(), request.price());

        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STOCK_NOT_FOUND));
        return toResponse(product, stock);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = findProductById(productId);
        stockRepository.findByProductId(productId).ifPresent(stockRepository::delete);
        productRepository.delete(product);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private ProductResponse toResponse(Product product, Stock stock) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                stock != null ? stock.getQuantity() : 0,
                product.getCreatedAt()
        );
    }
}
