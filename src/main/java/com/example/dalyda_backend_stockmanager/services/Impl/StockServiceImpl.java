package com.example.dalyda_backend_stockmanager.services.Impl;

import com.example.dalyda_backend_stockmanager.repositories.StockRepository;
import com.example.dalyda_backend_stockmanager.services.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    public Integer getTotalStock() {
        var stock = stockRepository.getTotalStock();
        if (stock == null) return 0;
        return stock;
    }

    @Override
    public Integer getTotalStockPrice() {
        var totalPrice = stockRepository.getTotalStockPrice();
        if (totalPrice == null) return 0;
        return totalPrice;
    }
}
