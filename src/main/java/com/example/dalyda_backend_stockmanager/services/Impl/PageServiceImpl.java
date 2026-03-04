package com.example.dalyda_backend_stockmanager.services.Impl;

import com.example.dalyda_backend_stockmanager.dtos.PageDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImpl {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 100;

    public Pageable getPageable(PageDto pageDto) {
        int page = pageDto.getPage() != null ? pageDto.getPage() : DEFAULT_PAGE;
        int size = pageDto.getSize() != null ? pageDto.getSize() : DEFAULT_SIZE;

        if (page < 0) {
            page = DEFAULT_PAGE;
        }
        if (size <= 0) {
            size = DEFAULT_SIZE;
        }
        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }

        return PageRequest.of(page, size);
    }

}
