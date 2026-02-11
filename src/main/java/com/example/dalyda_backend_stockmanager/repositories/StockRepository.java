package com.example.dalyda_backend_stockmanager.repositories;

import com.example.dalyda_backend_stockmanager.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {
    @Query("SELECT SUM(s.quantity) FROM Stock s")
    Integer getTotalStock();

    @Query("SELECT SUM(s.price) FROM Stock s")
    Integer getTotalStockPrice();
}
