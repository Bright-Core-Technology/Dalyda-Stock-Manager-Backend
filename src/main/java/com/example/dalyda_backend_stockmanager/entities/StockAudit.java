package com.example.dalyda_backend_stockmanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockAudit {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private UUID stockId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockAuditAction action;

    @Column(nullable = false)
    private String performedBy;

    @Column(nullable = false)
    private LocalDateTime performedAt;

    @Lob
    private String details;

    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (performedAt == null) {
            performedAt = LocalDateTime.now();
        }
    }
}

