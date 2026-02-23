package com.example.dalyda_backend_stockmanager.services;

import com.example.dalyda_backend_stockmanager.dtos.PageDto;
import com.example.dalyda_backend_stockmanager.dtos.StockDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StockService {
    Integer getTotalStock();

    Integer getTotalStockPrice();

    Page<StockDto.viewStockDto> ViewAllStock(PageDto pageDto);

    Page<StockDto.viewStockDto> searchStock(String keyword, PageDto pageDto);

    List<String> getDistinctContainerNames();
}
