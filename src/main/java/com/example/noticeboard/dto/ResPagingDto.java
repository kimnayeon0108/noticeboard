package com.example.noticeboard.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class ResPagingDto<T> {

    private int page;
    private int pageSize;
    private int totalPage;
    private boolean hasNext;
    private long totalElements;
    private T contents;

    public static <T> ResPagingDto<T> of(Page<T> page) {

        return (ResPagingDto<T>) ResPagingDto.builder()
                .page(page.getPageable().getPageNumber() + 1)
                .pageSize(page.getSize())
                .totalPage(page.getTotalPages())
                .hasNext(page.hasNext())
                .totalElements(page.getTotalElements())
                .contents(page.getContent())
                .build();
    }
}
