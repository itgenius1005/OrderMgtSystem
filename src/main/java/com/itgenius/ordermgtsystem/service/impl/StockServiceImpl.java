package com.itgenius.ordermgtsystem.service.impl;

import com.itgenius.ordermgtsystem.domain.Stock;
import com.itgenius.ordermgtsystem.dto.request.UpdateStockRequest;
import com.itgenius.ordermgtsystem.dto.response.StockResponse;
import com.itgenius.ordermgtsystem.exception.BusinessException;
import com.itgenius.ordermgtsystem.exception.ErrorCode;
import com.itgenius.ordermgtsystem.repository.StockRepository;
import com.itgenius.ordermgtsystem.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    @Transactional(readOnly = true)
    public StockResponse getStockByProductId(Long productId) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STOCK_NOT_FOUND));
        return toResponse(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockResponse> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public StockResponse updateStock(Long productId, UpdateStockRequest request) {
        Stock stock = stockRepository.findByProductIdWithLock(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STOCK_NOT_FOUND));
        stock.updateQuantity(request.quantity());
        return toResponse(stock);
    }

    private StockResponse toResponse(Stock stock) {
        return new StockResponse(
                stock.getId(),
                stock.getProduct().getId(),
                stock.getProduct().getName(),
                stock.getQuantity(),
                stock.getUpdatedAt()
        );
    }
}
