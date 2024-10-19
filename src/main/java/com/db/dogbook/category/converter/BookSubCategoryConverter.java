package com.db.dogbook.category.converter;

import com.db.dogbook.book.bookDto.BookDto;
import com.db.dogbook.category.categoryDto.BookSubCategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.domain.BookSubCategory;
import com.db.dogbook.category.domain.SubCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BookSubCategoryConverter {

    private final CategoryConverter categoryConverter;


    // BookSubCategory -> BookSubCategoryDto 변환
    public BookSubCategoryDto toBookSubCategoryDto(BookSubCategory bookSubCategory) {
        return BookSubCategoryDto.builder()
                .id(bookSubCategory.getId())
                .bookDto(BookDto.builder()
                        .id(bookSubCategory.getBook().getId())
                        .build())
                .subCategoryDto(SubCategoryDto.builder()
                        .id(bookSubCategory.getSubCategory().getId())
                        .build())
                .build();
    }

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

}
