package com.example.noticeboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Builder
@Getter
@Setter
public class PagingResponse<T> {

    private int page;
    private int pageSize;
    private int totalPage;
    private boolean hasNext;
    private long totalElements;
    private T contents;

    public static <T> PagingResponse<T> of(Page<T> page) {

        return (PagingResponse<T>) PagingResponse.builder()
                .page(page.getPageable().getPageNumber() + 1)
                .pageSize(page.getSize())
                .totalPage(page.getTotalPages())
                .hasNext(page.hasNext())
                .totalElements(page.getTotalElements())
                .contents(page.getContent())
                .build();
    }
}
