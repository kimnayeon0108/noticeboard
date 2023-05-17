package com.example.noticeboard.service;

import com.example.noticeboard.domain.Category;
import com.example.noticeboard.dto.request.ReqCreateCategoryDto;
import com.example.noticeboard.dto.request.ReqUpdateCategoryDto;
import com.example.noticeboard.dto.response.ResCategoryDto;
import com.example.noticeboard.exception.BaseException;
import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.repository.CategoryRepository;
import com.example.noticeboard.repository.PostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostCategoryRepository postCategoryRepository;

    public ResCategoryDto addCategory(ReqCreateCategoryDto reqCreateCategoryDto) {

        if (categoryRepository.existsByParentIdAndName(reqCreateCategoryDto.getParentId(),
                reqCreateCategoryDto.getName())) {
            throw new BaseException(ErrorCode.DUPLICATED_CATEGORY);
        }

        Category parentCategory = null;

        if (reqCreateCategoryDto.getParentId() != null) {
            parentCategory = categoryRepository.findById(reqCreateCategoryDto.getParentId())
                                               .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_FOUND));
        }

        int depth = getDepth(parentCategory);

        Category category = Category.builder()
                                    .parentCategory(parentCategory)
                                    .name(reqCreateCategoryDto.getName())
                                    .depth(depth)
                                    .build();

        categoryRepository.save(category);

        return ResCategoryDto.of(category);
    }

    private int getDepth(Category parentCategory) {
        if (parentCategory == null) {
            return 1;
        }

        if (parentCategory.getDepth() == 1) {
            return 2;
        }

        if (parentCategory.getDepth() == 2) {
            return 3;
        }

        return 1;
    }

    public ResCategoryDto editCategory(long categoryId, ReqUpdateCategoryDto reqUpdateCategoryDto) {
        Category category = categoryRepository.findById(categoryId)
                                              .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_FOUND));

        category.update(reqUpdateCategoryDto.getName());

        return ResCategoryDto.of(category);
    }

    public void deleteCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                                              .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_FOUND));

        // 모든 하위 카테고리
        List<Long> childCategoryIds = findChildCategoryIds(category);

        boolean existPost = postCategoryRepository.existsByCategoryIdIn(childCategoryIds);

        if (existPost) {
            throw new BaseException(ErrorCode.UNDELETABLE_CATEGORY);
        }

        categoryRepository.deleteAllByIdInBatch(childCategoryIds);
    }

    private List<Long> findChildCategoryIds(Category category) {
        List<Long> allChildCategoryIds = new ArrayList<>();
        allChildCategoryIds.add(category.getId());

        List<Long> parentCategoryIds = new ArrayList<>(List.of(category.getId()));

        // 하위 카테고리 찾기
        for (int i = 0; i < Category.TOTAL_DEPTH - category.getDepth(); i++) {
            List<Long> childCategoryIds = categoryRepository.findIdsByParentCategoryIdIn(parentCategoryIds);
            childCategoryIds.forEach(id -> allChildCategoryIds.add(id));

            parentCategoryIds = childCategoryIds;
        }

        return allChildCategoryIds;
    }
}
