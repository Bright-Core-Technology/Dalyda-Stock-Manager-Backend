package com.example.dalyda_backend_stockmanager.services.Impl;

import com.example.dalyda_backend_stockmanager.dtos.PageDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImpl {

    public Pageable getPageable(PageDto pageDto) {
        int page = pageDto.getPage() != null ? pageDto.getPage() : 0;
        int size = pageDto.getSize() != null ? pageDto.getSize() : 10;
        return PageRequest.of(page, size);
    }

}
