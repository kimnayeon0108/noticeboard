package com.example.noticeboard.repository;

import com.example.noticeboard.domain.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
}
