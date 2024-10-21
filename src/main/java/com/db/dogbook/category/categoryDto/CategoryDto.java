package com.db.dogbook.category.categoryDto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String categoryName;
    private List<SubCategoryDto> subCategoryDtos;
}
