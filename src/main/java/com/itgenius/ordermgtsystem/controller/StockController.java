package com.itgenius.ordermgtsystem.controller;

import com.itgenius.ordermgtsystem.dto.request.UpdateStockRequest;
import com.itgenius.ordermgtsystem.dto.response.StockResponse;
import com.itgenius.ordermgtsystem.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockResponse>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<StockResponse> getStockByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getStockByProductId(productId));
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<StockResponse> updateStock(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateStockRequest request) {
        return ResponseEntity.ok(stockService.updateStock(productId, request));
    }
}
