package com.example.dalyda_backend_stockmanager.services.Impl;

import com.example.dalyda_backend_stockmanager.dtos.SalesDto;
import com.example.dalyda_backend_stockmanager.mappers.SalesMapper;
import com.example.dalyda_backend_stockmanager.repositories.SalesRepository;
import com.example.dalyda_backend_stockmanager.services.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;

    @Override
    public Integer getWeeklySales() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        var weeklySales = salesRepository.getWeeklySales(startDate, endDate);
        if (weeklySales == null) {
            return 0;
        }
        return weeklySales;
    }

    @Override
    public BigDecimal getWeeklySalesValue() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        var weeklySalesValue = salesRepository.getWeeklySalesValue(startDate, endDate);
        return Objects.requireNonNullElse(weeklySalesValue, BigDecimal.ZERO);
    }

    @Override
    public List<SalesDto.RecentSaleResponse> getRecentSales() {
        var top5Sales = salesRepository.findTop5ByOrderByDateDesc();
        return SalesMapper.map(top5Sales);
    }
}
