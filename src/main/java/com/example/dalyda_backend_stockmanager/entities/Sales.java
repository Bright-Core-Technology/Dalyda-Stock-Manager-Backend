package com.example.dalyda_backend_stockmanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sales {
    @Id
    private UUID id = UUID.randomUUID();
    private LocalDate date;
    private String code;
    private String name;
    private Integer quantity;
    private Double price;
    private Double totalPrice;
    @Enumerated(EnumType.STRING)
    private ContainerWeights weight;
    private String containerName;
    private String recordedBy;
}
