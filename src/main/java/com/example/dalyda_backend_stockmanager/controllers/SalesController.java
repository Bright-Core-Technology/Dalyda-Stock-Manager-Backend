package com.example.dalyda_backend_stockmanager.controllers;

import com.example.dalyda_backend_stockmanager.dtos.SalesDto;
import com.example.dalyda_backend_stockmanager.responses.GenericResponse;
import com.example.dalyda_backend_stockmanager.services.SalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Sales Controller", description = "Handles Sales")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/sales")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class SalesController {

    private final SalesService salesService;

    //weekly sales  of all the bales
    @Operation(summary = "Weekly Sales", description = "View Weekly Sales")
    @GetMapping("/total")
    public ResponseEntity<GenericResponse<Integer>> weeklySales() {
        var weeklySales = salesService.getWeeklySales();
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("This is the total Weekly Sales", weeklySales));
    }

    // Recent Sales table, Display 5 recent sales
    @Operation(summary = "Recent Sale", description = "Display 5 recent sales")
    @GetMapping("/recent")
    public ResponseEntity<GenericResponse<List<SalesDto.RecentSaleResponse>>> getRecentSales() {
        var recentSales = salesService.getRecentSales();
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("Recent Sales fetched successfully", recentSales));
    }

    // Weekly Sales Value
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Weekly Sales Value", description = "The value of all the weekly sales")
    @GetMapping("/value")
    public ResponseEntity<GenericResponse<Integer>> weeklySalesValue() {
        var weeklySalesValue = salesService.getWeeklySalesValue();
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("This is the total Weekly Sales", weeklySalesValue));
    }
}
