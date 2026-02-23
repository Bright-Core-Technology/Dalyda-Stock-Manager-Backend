package com.example.dalyda_backend_stockmanager.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PageDto {

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 10;
}
