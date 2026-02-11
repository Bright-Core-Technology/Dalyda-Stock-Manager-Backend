package com.example.dalyda_backend_stockmanager.services.Impl;

import com.example.dalyda_backend_stockmanager.repositories.SalesRepository;
import com.example.dalyda_backend_stockmanager.services.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
