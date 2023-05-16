package com.example.noticeboard.repository;

import com.example.noticeboard.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryCustomRepository {

    @Query("SELECT c.id FROM Category c WHERE c.parentCategory.id IN (:categoryIds)")
    List<Long> findIdsByParentCategoryIdIn(@Param("categoryIds") List<Long> categoryIds);
}
