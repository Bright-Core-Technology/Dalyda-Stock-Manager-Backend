package com.example.dalyda_backend_stockmanager.controllers;

import com.example.dalyda_backend_stockmanager.dtos.PageDto;
import com.example.dalyda_backend_stockmanager.dtos.StockDto;
import com.example.dalyda_backend_stockmanager.responses.GenericResponse;
import com.example.dalyda_backend_stockmanager.services.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Stock Controller", description = "Handles Stock")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/stock")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")

public class StockController {

    private final StockService stockService;

    // total stock of all the bales
    @Operation(summary = "Total of Stock", description = "View the total number of Stock")
    @GetMapping("/total")
    public ResponseEntity<GenericResponse<Integer>> totalStock() {
        var totalStock = stockService.getTotalStock();
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("This is the total Stock Number", totalStock));
    }

    // total stock price of all the bales
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Total Stock Value", description = "View the total stock value")
    @GetMapping("/value")
    public ResponseEntity<GenericResponse<Integer>> totalStockPrice() {
        var totalStockPrice = stockService.getTotalStockPrice();
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("This is the total Stock Price", totalStockPrice));
    }

    // View all Stock with Pagination
    @Operation(summary = "All Stock Available", description = "All Items in Stock that are available")
    @GetMapping("/")
    public ResponseEntity<GenericResponse<Page<StockDto.viewStockDto>>> viewAllStock(PageDto page) {
        var viewAllStock = stockService.ViewAllStock(page);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("Available Stock", viewAllStock));
    }

    // Search Stock By Keyword with Pagination
    // to be used when searching by container name also, change the keyword to match the container name
    @Operation(summary = "Search Stock by Keyword", description = "Search the Stock by Name, and code of the Bale and the container name")
    @GetMapping("/search")
    public ResponseEntity<GenericResponse<Page<StockDto.viewStockDto>>> searchStock(@RequestParam String keyword, PageDto page) {
        var searchStock = stockService.searchStock(keyword, page);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("Available Stock", searchStock));
    }

    @Operation(summary = "Get Distinct Container Name", description = "Return a List of Distinct Container names in the Database")
    @GetMapping("/container/names")
    public ResponseEntity<GenericResponse<List<String>>> getDistinctContainerNames() {
        var containerNames = stockService.getDistinctContainerNames();
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("Container Names:", containerNames));
    }
}
