package com.example.dalyda_backend_stockmanager.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SalesDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecentSaleResponse {
        private String code;
        private String name;
        private LocalDate date;
        private String containerName;
        private String recordedBy;
        private Integer quantity;
        private BigDecimal totalPrice;
    }
}
