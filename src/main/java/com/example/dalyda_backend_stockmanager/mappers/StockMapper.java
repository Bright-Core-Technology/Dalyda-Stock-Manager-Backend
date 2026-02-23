package com.example.dalyda_backend_stockmanager.mappers;

import com.example.dalyda_backend_stockmanager.dtos.StockDto;
import com.example.dalyda_backend_stockmanager.entities.Stock;

public class StockMapper {
    public static StockDto.viewStockDto map(Stock stock) {
        return new StockDto.viewStockDto(stock.getCode(), stock.getName(), stock.getContainer_name(), stock.getQuantity(), stock.getWeight(), stock.getPrice());
    }
}
