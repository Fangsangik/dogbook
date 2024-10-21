package com.db.dogbook.category.service;

import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.converter.SubCategoryConverter;
import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.domain.SubCategory;
import com.db.dogbook.category.repository.CategoryRepository;
import com.db.dogbook.category.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryConverter subCategoryConverter;
    private final SubCategoryRepository subCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoryDto> getSubCategoriesByCategoryId(Long categoryId) {
        // 카테고리 존재 여부 확인
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        List<SubCategory> subCategories = subCategoryRepository.findByCategoryId(category.getId());

        return subCategories.stream()
                .map(subCategoryConverter::toSubCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto) {
        SubCategory subCategory = subCategoryConverter.toSubCategory(subCategoryDto);
        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        return subCategoryConverter.toSubCategoryDto(savedSubCategory);
    }

    @Override
    @Transactional
    public SubCategoryDto assignToCategory(SubCategoryDto subCategoryDto, Long categoryId) {
        // SubCategoryDto의 ID가 null인 경우 예외 처리
        if (subCategoryDto.getId() == null) {
            throw new IllegalArgumentException("SubCategory ID must not be null");
        }

        // Category 확인
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        SubCategory subCategory = subCategoryRepository.findById(subCategoryDto.getId())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        // SubCategory에 Category 매핑
        subCategory.setCategory(category);

        // 업데이트된 SubCategory 저장
        SubCategory updatedSubCategory = subCategoryRepository.save(subCategory);

        return subCategoryConverter.toSubCategoryDto(updatedSubCategory);
    }
}
