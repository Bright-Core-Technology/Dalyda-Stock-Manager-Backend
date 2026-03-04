package com.example.dalyda_backend_stockmanager.services;

import com.example.dalyda_backend_stockmanager.dtos.PageDto;
import com.example.dalyda_backend_stockmanager.dtos.StockAuditDto;
import com.example.dalyda_backend_stockmanager.dtos.StockDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface StockService {
    Integer getTotalStock();

    BigDecimal getTotalStockPrice();

    Page<StockDto.ViewStockDto> viewAllStock(PageDto pageDto);

    Page<StockDto.ViewStockDto> searchStock(String keyword, String containerName, PageDto pageDto);

    List<String> getDistinctContainerNames();

    StockDto.ViewStockDto addStock(StockDto.AddStockDto addStockDto, String recordedBy);

    void deleteStock(UUID stockId, String deletedBy);

    StockDto.ViewStockDto updateStock(UUID stockId, StockDto.UpdateStockDto updateStockDto, String updatedBy);

    StockDto.BulkUploadResponse bulkUploadFromExcel(MultipartFile file, String recordedBy);

    List<StockAuditDto> getStockAuditTrail(UUID stockId);
}
