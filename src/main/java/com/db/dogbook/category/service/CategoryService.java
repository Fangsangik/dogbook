package com.db.dogbook.category.service;

import com.db.dogbook.category.categoryDto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(CategoryDto categoryDto);

    CategoryDto findCategoryByCategoryName(String name);

    List<CategoryDto> getAllCategories();
}
