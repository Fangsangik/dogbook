package com.db.dogbook.category.converter;

import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.domain.Category;
import org.springframework.stereotype.Component;


@Component
public class CategoryConverter {

    // Category -> CategoryDto 변환
    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }

    // CategoryDto -> Category 변환
    public Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .categoryName(categoryDto.getCategoryName())
                .build();
    }
}
