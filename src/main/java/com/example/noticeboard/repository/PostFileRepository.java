package com.example.noticeboard.repository;

import com.example.noticeboard.domain.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {

    List<PostFile> findAllByPostId(long postId);
}
