package com.example.noticeboard.repository;

import com.example.noticeboard.domain.Post;
import com.example.noticeboard.dto.ReqPostListParamsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCustomRepository {

    Page<Post> findAllByConditions(ReqPostListParamsDto requestParams, Pageable pageable);
}
