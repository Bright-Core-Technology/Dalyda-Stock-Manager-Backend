package com.example.dalyda_backend_stockmanager.controllers;

import com.example.dalyda_backend_stockmanager.responses.GenericResponse;
import com.example.dalyda_backend_stockmanager.services.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Stock Controller", description = "Handles Stock")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/stock")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")

public class StockController {

    private final StockService stockService;

    // total stock of all the bales
    @Operation(summary = "Total of Stock", description = "View the total number of Stock")
    @GetMapping("/total/stock")
    public ResponseEntity<GenericResponse<Integer>> totalStock() {
        var totalStock = stockService.getTotalStock();
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("This is the total Stock Number", totalStock));
    }

    // total stock price of all the bales
    @Operation(summary = "Total Stock Price", description = "View the total stock price")
    @GetMapping("/total/stock/price")
    public ResponseEntity<GenericResponse<Integer>> totalStockPrice() {
        var totalStockPrice = stockService.getTotalStockPrice();
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("This is the total Stock Price", totalStockPrice));
    }
}
