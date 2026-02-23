package com.example.dalyda_backend_stockmanager.services.Impl;

import com.example.dalyda_backend_stockmanager.dtos.SalesDto;
import com.example.dalyda_backend_stockmanager.mappers.SalesMapper;
import com.example.dalyda_backend_stockmanager.repositories.SalesRepository;
import com.example.dalyda_backend_stockmanager.services.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;

    @Override
    public Integer getWeeklySales() { // fix the logic of this
        var weeklySales = salesRepository.getWeeklySales();
        if (weeklySales == null) return 0;
        return weeklySales;
    }

    @Override
    public Integer getWeeklySalesValue() {
        var weeklySalesValue = salesRepository.getWeeklySalesValue();
        if (weeklySalesValue == null) return 0;
        return weeklySalesValue;
    }

    @Override
    public List<SalesDto.RecentSaleResponse> getRecentSales() {
        var top5Sales = salesRepository.findTop5ByOrderByDateDesc();
        return SalesMapper.map(top5Sales);
    }
}
