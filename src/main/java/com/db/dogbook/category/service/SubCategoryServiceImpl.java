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

        // 해당 카테고리에 속한 서브 카테고리 목록 조회
        List<SubCategory> subCategories = subCategoryRepository.findByCategoryId(category.getId());

        return subCategories.stream()
                .map(subCategoryConverter::toSubCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto) {
        // SubCategory 변환 및 저장
        SubCategory subCategory = subCategoryConverter.toSubCategory(subCategoryDto);
        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        return subCategoryConverter.toSubCategoryDto(savedSubCategory);
    }

    @Override
    @Transactional
    public SubCategoryDto assignToCategory(Long subCategoryId, Long categoryId) {
        // SubCategory ID 검증
        if (subCategoryId == null) {
            throw new IllegalArgumentException("SubCategory ID must not be null");
        }

        // 카테고리 확인
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // SubCategory 조회
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("SubCategory not found"));

        // 필요할 때만 SubCategory에 Category 매핑
        if (!category.equals(subCategory.getCategory())) {
            subCategory.setCategory(category);
        }

        // 업데이트된 SubCategory 저장
        SubCategory updatedSubCategory = subCategoryRepository.save(subCategory);

        return subCategoryConverter.toSubCategoryDto(updatedSubCategory);
    }
}
