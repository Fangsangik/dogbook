package com.db.dogbook.category.converter;

import com.db.dogbook.book.bookDto.BookConverter;
import com.db.dogbook.book.bookDto.BookDto;
import com.db.dogbook.book.model.Book;
import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.domain.SubCategory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubCategoryConverter {


    public SubCategoryDto toSubCategoryDto(SubCategory subCategory) {
        if (subCategory == null) {
            return null; // null 체크 추가
        }

        return SubCategoryDto.builder()
                .id(subCategory.getId())
                .subCategoryName(subCategory.getSubCategoryName())
                .categoryDto(subCategory.getCategory() != null ? // null 체크
                        CategoryDto.builder()
                                .id(subCategory.getCategory().getId())
                                .categoryName(subCategory.getCategory().getCategoryName())
                                .build() : null) // Category가 null인 경우 null 설정
                .bookDtos(subCategory.getBooks() != null ? subCategory.getBooks().stream()
                        .map(book -> BookDto.builder()
                                .id(book.getId())
                                .bookName(book.getBookName())
                                .author(book.getAuthor())
                                .price(book.getPrice())
                                .build())
                        .collect(Collectors.toList()) : Collections.emptyList()) // null 방지
                .build();
    }

    public SubCategory toSubCategory(SubCategoryDto subCategoryDto) {
        if (subCategoryDto == null) {
            throw new IllegalArgumentException("SubCategoryDto cannot be null");
        }

        Category category = null;
        if (subCategoryDto.getCategoryDto() != null) {
            category = Category.builder()
                    .id(subCategoryDto.getCategoryDto().getId())
                    .categoryName(subCategoryDto.getCategoryDto().getCategoryName())
                    .build();  // Category가 null이 아닐 경우에만 설정
        }

        return SubCategory.builder()
                .id(subCategoryDto.getId())
                .subCategoryName(subCategoryDto.getSubCategoryName())
                .category(category) // 선택적 관계 설정
                .build();
    }
}
