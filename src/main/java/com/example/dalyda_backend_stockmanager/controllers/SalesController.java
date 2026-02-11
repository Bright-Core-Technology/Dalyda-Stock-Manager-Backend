package com.example.dalyda_backend_stockmanager.controllers;

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

@Tag(name = "Sales Controller", description = "Handles Sales")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/sales")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class SalesController {

    private final SalesService salesService;

    // total stock of all the bales
    @Operation(summary = "Weekly Sales", description = "View Weekly Sales")
    @GetMapping("/total/weekly/sales")
    public ResponseEntity<GenericResponse<Integer>> weeklySales() {
        var weeklySales = salesService.getWeeklySales();
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("This is the total Weekly Sales", weeklySales));
    }
}
