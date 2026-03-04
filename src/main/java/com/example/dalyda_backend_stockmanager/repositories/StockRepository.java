package com.example.dalyda_backend_stockmanager.repositories;

import com.example.dalyda_backend_stockmanager.entities.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {
    @Query("SELECT SUM(s.quantity) FROM Stock s")
    Integer getTotalStock();

    @Query("SELECT SUM(s.price) FROM Stock s")
    BigDecimal getTotalStockPrice();

    @Query("SELECT s FROM Stock s")
    Page<Stock> findAllStock(Pageable pageable);

    @Query("""
             SELECT s FROM Stock s
             WHERE
                 (
                    :keyword IS NULL OR
                    LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(s.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
                 )
                 AND
                 (
                    :containerName IS NULL OR
                    LOWER(s.containerName) = LOWER(:containerName)
                 )
            """)
    Page<Stock> searchStock(@Param("keyword") String keyword,
                            @Param("containerName") String containerName,
                            Pageable pageable);

    @Query("SELECT DISTINCT s.containerName FROM Stock s ORDER BY s.containerName")
    List<String> findDistinctContainerNames();
}
