package com.example.dalyda_backend_stockmanager.services.Impl;

import com.example.dalyda_backend_stockmanager.dtos.PageDto;
import com.example.dalyda_backend_stockmanager.dtos.StockDto;
import com.example.dalyda_backend_stockmanager.mappers.StockMapper;
import com.example.dalyda_backend_stockmanager.repositories.StockRepository;
import com.example.dalyda_backend_stockmanager.services.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final PageServiceImpl pageServiceImpl;

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

    @Override
    public Page<StockDto.viewStockDto> ViewAllStock(PageDto pageDto) {
        var pageable = pageServiceImpl.getPageable(pageDto);
        var usersPage = stockRepository.findAllStock(pageable);
        return usersPage.map(StockMapper::map);
    }

    @Override
    public Page<StockDto.viewStockDto> searchStock(String keyword, PageDto pageDto) {
        var pageable = pageServiceImpl.getPageable(pageDto);
        if ("ALL".equalsIgnoreCase(keyword)) {
            return stockRepository.findAll(pageable)
                    .map(StockMapper::map);
        }
        return stockRepository.searchStock(keyword.toLowerCase(), pageable)
                .map(StockMapper::map);
    }

    @Override
    public List<String> getDistinctContainerNames() {
        return stockRepository.findDistinctContainerNames();
    }
}
