package com.itgenius.ordermgtsystem.service;

import com.itgenius.ordermgtsystem.dto.request.UpdateStockRequest;
import com.itgenius.ordermgtsystem.dto.response.StockResponse;

import java.util.List;

public interface StockService {

    StockResponse getStockByProductId(Long productId);

    List<StockResponse> getAllStocks();

    StockResponse updateStock(Long productId, UpdateStockRequest request);
}
