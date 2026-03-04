package com.example.dalyda_backend_stockmanager.services;

import com.example.dalyda_backend_stockmanager.dtos.SalesDto;

import java.math.BigDecimal;
import java.util.List;

public interface SalesService {
    Integer getWeeklySales();

    BigDecimal getWeeklySalesValue();

    List<SalesDto.RecentSaleResponse> getRecentSales();
}
