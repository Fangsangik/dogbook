package com.db.dogbook.category.converter;

import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.domain.Category;
import org.springframework.stereotype.Component;


@Component
public class CategoryConverter {


    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }

    public Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .categoryName(categoryDto.getCategoryName())
                .build();
    }
}
