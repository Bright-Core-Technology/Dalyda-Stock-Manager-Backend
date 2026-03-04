package com.example.dalyda_backend_stockmanager.repositories;

import com.example.dalyda_backend_stockmanager.entities.StockAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockAuditRepository extends JpaRepository<StockAudit, UUID> {
    List<StockAudit> findByStockIdOrderByPerformedAtDesc(UUID stockId);
}

