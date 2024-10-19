package com.db.dogbook.category.categoryDto;

import com.db.dogbook.category.domain.SubCategory;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String categoryName;
    private List<SubCategory> subCategories;
}
