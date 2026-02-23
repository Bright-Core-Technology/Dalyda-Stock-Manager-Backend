package com.example.dalyda_backend_stockmanager.services;

import com.example.dalyda_backend_stockmanager.dtos.SalesDto;

import java.util.List;

public interface SalesService {
    Integer getWeeklySales();

    Integer getWeeklySalesValue();

    List<SalesDto.RecentSaleResponse> getRecentSales();
}
