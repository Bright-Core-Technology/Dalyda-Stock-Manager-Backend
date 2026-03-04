package com.example.dalyda_backend_stockmanager.dtos;

import com.example.dalyda_backend_stockmanager.entities.StockAuditAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockAuditDto {
    private UUID id;
    private UUID stockId;
    private StockAuditAction action;
    private String performedBy;
    private LocalDateTime performedAt;
    private String details;
}

