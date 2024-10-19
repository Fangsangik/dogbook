package com.db.dogbook.category.converter;

import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.domain.SubCategory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SubCategoryConverter {

    private final CategoryConverter categoryConverter;

    // SubCategory -> SubCategoryDto 변환
    public SubCategoryDto toSubCategoryDto(SubCategory subCategory) {
        // null 체크
        if (subCategory == null || subCategory.getCategory() == null) {
            throw new RuntimeException("SubCategory or Category is missing");
        }

        return SubCategoryDto.builder()
                .id(subCategory.getId())
                .subCategoryName(subCategory.getSubCategoryName())
                .categoryDto(categoryConverter.toCategoryDto(subCategory.getCategory())) // CategoryDto 변환
                .build();
    }
    // SubCategoryDto -> SubCategory 변환
    public SubCategory toSubCategory(SubCategoryDto subCategoryDto) {
        return SubCategory.builder()
                .id(subCategoryDto.getId())
                .subCategoryName(subCategoryDto.getSubCategoryName())
                .build();
    }
}
