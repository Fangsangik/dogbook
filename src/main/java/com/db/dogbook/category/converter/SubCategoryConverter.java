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

import java.util.stream.Collectors;


@Component
public class SubCategoryConverter {

    // SubCategory -> SubCategoryDto 변환
    public SubCategoryDto toSubCategoryDto(SubCategory subCategory) {
        return SubCategoryDto.builder()
                .id(subCategory.getId())
                .subCategoryName(subCategory.getSubCategoryName())
                .categoryDto(CategoryDto.builder()  // 빌더 패턴 사용
                        .id(subCategory.getCategory().getId())
                        .categoryName(subCategory.getCategory().getCategoryName())
                        .build())
                .bookDtos(subCategory.getBooks().stream()
                        .map(book -> BookDto.builder()  // 빌더 패턴 사용
                                .id(book.getId())
                                .bookName(book.getBookName())
                                .author(book.getAuthor())
                                .price(book.getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    // SubCategoryDto -> SubCategory 변환
    public SubCategory toSubCategory(SubCategoryDto subCategoryDto) {
        return SubCategory.builder()
                .id(subCategoryDto.getId())
                .subCategoryName(subCategoryDto.getSubCategoryName())
                .category(Category.builder()  // 빌더 패턴 사용
                        .id(subCategoryDto.getCategoryDto().getId())
                        .categoryName(subCategoryDto.getCategoryDto().getCategoryName())
                        .build())
                .books(subCategoryDto.getBookDtos().stream()
                        .map(bookDto -> Book.builder()  // 빌더 패턴 사용
                                .id(bookDto.getId())
                                .bookName(bookDto.getBookName())
                                .author(bookDto.getAuthor())
                                .price(bookDto.getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
