package com.example.dalyda_backend_stockmanager.repositories;

import com.example.dalyda_backend_stockmanager.entities.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SalesRepository extends JpaRepository<Sales, UUID> {
    @Query("SELECT SUM(s.quantity) FROM Sales s")
    Integer getWeeklySales();

}
