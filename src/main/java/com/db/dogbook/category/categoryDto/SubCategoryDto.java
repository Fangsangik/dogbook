package com.db.dogbook.category.categoryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDto {

    private Long id;
    private String subCategoryName;
    private CategoryDto categoryDto;
    private List<BookSubCategoryDto> bookSubCategoryDtos;
 }
