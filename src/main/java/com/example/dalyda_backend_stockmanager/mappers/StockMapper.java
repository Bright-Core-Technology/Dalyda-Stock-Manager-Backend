package com.example.dalyda_backend_stockmanager.mappers;

import com.example.dalyda_backend_stockmanager.dtos.StockDto;
import com.example.dalyda_backend_stockmanager.entities.Stock;

public class StockMapper {
    public static StockDto.ViewStockDto map(Stock stock) {
        return new StockDto.ViewStockDto(stock.getCode(), stock.getName(), stock.getContainerName(), stock.getQuantity(), stock.getWeight(), stock.getPrice());
    }

    public static Stock map(StockDto.AddStockDto addStockDto, String recordedBy) {
        return new Stock(addStockDto.getCode(), addStockDto.getName(), addStockDto.getContainerName(), addStockDto.getQuantity(), addStockDto.getWeight(), addStockDto.getPrice(), recordedBy);

    }
}
