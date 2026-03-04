package com.example.dalyda_backend_stockmanager.controllers;

import com.example.dalyda_backend_stockmanager.dtos.PageDto;
import com.example.dalyda_backend_stockmanager.dtos.StockAuditDto;
import com.example.dalyda_backend_stockmanager.dtos.StockDto;
import com.example.dalyda_backend_stockmanager.responses.GenericResponse;
import com.example.dalyda_backend_stockmanager.services.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Tag(name = "Stock Controller", description = "Handles Stock")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/stock")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")

public class StockController {

    private final StockService stockService;

    // Add new stock item
    @Operation(summary = "Add New Stock", description = "Add a single stock item to inventory")
    @PostMapping("/add")
    public ResponseEntity<GenericResponse<StockDto.ViewStockDto>> addStock(
            @Valid @RequestBody StockDto.AddStockDto addStockDto,
            Authentication authentication) {
        String recordedBy = authentication.getName();
        var newStock = stockService.addStock(addStockDto, recordedBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse<>("Stock item added successfully", newStock));
    }

    // Delete stock item by ID
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete Stock", description = "Delete a stock item by its ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteStock(@PathVariable("id") UUID stockId, Authentication authentication) {
        stockService.deleteStock(stockId, authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("Stock item deleted successfully", null));
    }

    // Update stock item by ID
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update Stock", description = "Update one or more fields of an existing stock item")
    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse<StockDto.ViewStockDto>> updateStock(
            @PathVariable("id") UUID stockId,
            @Valid @RequestBody StockDto.UpdateStockDto updateStockDto,
            Authentication authentication) {
        var updatedStock = stockService.updateStock(stockId, updateStockDto, authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("Stock item updated successfully", updatedStock));
    }

    // Bulk upload stock items from Excel file
    @Operation(summary = "Bulk Upload Stock", description = "Upload multiple stock items from an Excel file (.xlsx)")
    @PostMapping(value = "/bulk-upload", consumes = "multipart/form-data")
    public ResponseEntity<GenericResponse<StockDto.BulkUploadResponse>> bulkUploadStock(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        String recordedBy = authentication.getName();
        var uploadResult = stockService.bulkUploadFromExcel(file, recordedBy);

        // Check if validation failed (no rows processed and errors exist)
        if (uploadResult.getTotalRows() == 0 && !uploadResult.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponse<>(uploadResult.getErrors().get(0), uploadResult));
        }

        String message = String.format("Upload completed: %d total rows, %d successful, %d failed",
                uploadResult.getTotalRows(), uploadResult.getSuccessfulRows(), uploadResult.getFailedRows());

        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(message, uploadResult));
    }

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
    public ResponseEntity<GenericResponse<BigDecimal>> totalStockPrice() {
        var totalStockPrice = stockService.getTotalStockPrice();
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("This is the total Stock Price", totalStockPrice));
    }

    // View all Stock with Pagination
    @Operation(summary = "All Stock Available", description = "All Items in Stock that are available")
    @GetMapping("/")
    public ResponseEntity<GenericResponse<Page<StockDto.ViewStockDto>>> viewAllStock(PageDto page) {
        var viewAllStock = stockService.viewAllStock(page);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("Available Stock", viewAllStock));
    }

    // Search Stock By Item Name and Code with Pagination
    // to be used when searching by container name also, change the keyword to match the container name
    @Operation(summary = "Search Stock", description = "Search stock by item code/name and/or filter by container name")
    @GetMapping("/search")
    public ResponseEntity<GenericResponse<Page<StockDto.ViewStockDto>>> searchStock(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String containerName,
            PageDto page) {
        var searchStock = stockService.searchStock(keyword, containerName, page);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("Available Stock", searchStock));
    }

    @Operation(summary = "Get Distinct Container Name", description = "Return a List of Distinct Container names in the Database")
    @GetMapping("/container/names")
    public ResponseEntity<GenericResponse<List<String>>> getDistinctContainerNames() {
        var containerNames = stockService.getDistinctContainerNames();
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("Container Names:", containerNames));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Stock Audit Trail", description = "Get audit trail (add/update/delete actions) for a stock item")
    @GetMapping("/{id}/audit-trail")
    public ResponseEntity<GenericResponse<List<StockAuditDto>>> getStockAuditTrail(@PathVariable("id") UUID stockId) {
        var audits = stockService.getStockAuditTrail(stockId);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>("Stock audit trail fetched successfully", audits));
    }
}
