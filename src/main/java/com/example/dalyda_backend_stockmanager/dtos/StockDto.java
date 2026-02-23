package com.example.dalyda_backend_stockmanager.dtos;

import com.example.dalyda_backend_stockmanager.entities.ContainerWeights;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class StockDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class viewStockDto {
        private String code;
        private String name;
        private String containerName;
        private Integer quantity;
        private ContainerWeights weight;
        private Double Price;
    }
}
