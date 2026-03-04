package com.example.dalyda_backend_stockmanager.dtos;

import com.example.dalyda_backend_stockmanager.entities.ContainerWeights;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

public class StockDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewStockDto {
        private String code;
        private String name;
        private String containerName;
        private Integer quantity;
        private ContainerWeights weight;
        private BigDecimal price;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddStockDto {
        @NotBlank(message = "Item code is required")
        private String code;

        @NotBlank(message = "Item name is required")
        private String name;

        @NotBlank(message = "Container name is required")
        private String containerName;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        @NotNull(message = "Weight is required")
        private ContainerWeights weight;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        private BigDecimal price;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateStockDto {
        private String code;
        private String name;
        private String containerName;
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
        private ContainerWeights weight;
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        private BigDecimal price;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BulkUploadResponse {
        private int totalRows;
        private int successfulRows;
        private int failedRows;
        private List<String> errors;
        private List<ViewStockDto> addedItems;
    }
}
