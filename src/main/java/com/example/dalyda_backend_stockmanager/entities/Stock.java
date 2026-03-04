package com.example.dalyda_backend_stockmanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    private UUID id = UUID.randomUUID();

    private String code;
    private String name;
    private Integer quantity;

    @Column(name = "container_name")
    private String containerName;

    @Enumerated(EnumType.STRING)
    private ContainerWeights weight;

    private BigDecimal price;

    private String recordedBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String lastUpdatedBy;

    private LocalDateTime lastUpdatedAt;

    public Stock(String code, String name, String containerName, Integer quantity, ContainerWeights weight, BigDecimal price, String recordedBy) {
        this.code = code;
        this.name = name;
        this.containerName = containerName;
        this.quantity = quantity;
        this.weight = weight;
        this.price = price;
        this.recordedBy = recordedBy;
    }

    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
