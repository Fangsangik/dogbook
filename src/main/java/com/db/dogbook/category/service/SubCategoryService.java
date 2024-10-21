package com.db.dogbook.category.service;

import com.db.dogbook.category.categoryDto.SubCategoryDto;

import java.util.List;

public interface SubCategoryService {

    SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto);
    List<SubCategoryDto> getSubCategoriesByCategoryId(Long categoryId);
    SubCategoryDto assignToCategory(SubCategoryDto subCategoryDto, Long categoryId);
}

