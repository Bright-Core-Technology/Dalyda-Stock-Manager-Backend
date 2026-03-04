package com.example.dalyda_backend_stockmanager.repositories;

import com.example.dalyda_backend_stockmanager.entities.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface SalesRepository extends JpaRepository<Sales, UUID> {
    @Query("SELECT SUM(s.quantity) FROM Sales s WHERE s.date BETWEEN :startDate AND :endDate")
    Integer getWeeklySales(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(s.totalPrice) FROM Sales s WHERE s.date BETWEEN :startDate AND :endDate")
    BigDecimal getWeeklySalesValue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Sales> findTop5ByOrderByDateDesc();
}
