package com.example.dalyda_backend_stockmanager.repositories;

import com.example.dalyda_backend_stockmanager.entities.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {
    @Query("SELECT SUM(s.quantity) FROM Stock s")
    Integer getTotalStock();

    @Query("SELECT SUM(s.price) FROM Stock s")
    Integer getTotalStockPrice();

    @Query("SELECT s FROM Stock s")
    Page<Stock> findAllStock(Pageable pageable);

    @Query("""
             SELECT s FROM Stock s
             WHERE
                 LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                 LOWER(s.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                 LOWER(s.container_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Stock> searchStock(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT s.container_name FROM Stock s ORDER BY s.container_name")
    List<String> findDistinctContainerNames();
}
