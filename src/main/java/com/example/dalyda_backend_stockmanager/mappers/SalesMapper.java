package com.example.dalyda_backend_stockmanager.mappers;

import com.example.dalyda_backend_stockmanager.dtos.SalesDto;
import com.example.dalyda_backend_stockmanager.entities.Sales;

import java.util.List;
import java.util.stream.Collectors;

public class SalesMapper {
    public static SalesDto.RecentSaleResponse map(Sales sale) {
        return new SalesDto.RecentSaleResponse(sale.getCode(), sale.getName(), sale.getDate(), sale.getContainerName(), sale.getRecordedBy(), sale.getQuantity(), sale.getTotalPrice());
    }

    public static List<SalesDto.RecentSaleResponse> map(List<Sales> sales) {
        return sales.stream()
                .map(SalesMapper::map)
                .collect(Collectors.toList());
    }
}
