package com.db.dogbook.book.bookDto;

import com.db.dogbook.book.model.Book;
import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.converter.CategoryConverter;
import com.db.dogbook.category.converter.SubCategoryConverter;
import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.domain.SubCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookConverter {

    public BookDto toBookDto(Book book) {
        // Category나 SubCategory에 대한 변환은 다른 컨버터에게 맡기지 않고,
        // 필요한 데이터만 가져와서 변환 처리
        return BookDto.builder()
                .id(book.getId())
                .bookName(book.getBookName())
                .author(book.getAuthor())
                .price(book.getPrice())
                .categoryDto(CategoryDto.builder()  // 필요한 필드만 변환
                        .id(book.getCategory().getId())
                        .categoryName(book.getCategory().getCategoryName())
                        .build())
                .subCategoryDto(SubCategoryDto.builder()  // 필요한 필드만 변환
                        .id(book.getSubCategory().getId())
                        .subCategoryName(book.getSubCategory().getSubCategoryName())
                        .build())
                .build();
    }

    public Book toBook(BookDto bookDto) {
        return Book.builder()
                .id(bookDto.getId())
                .bookName(bookDto.getBookName())
                .author(bookDto.getAuthor())
                .price(bookDto.getPrice())
                // Category와 SubCategory는 DTO의 필드만 사용하여 변환
                .category(Category.builder()
                        .id(bookDto.getCategoryDto().getId())
                        .categoryName(bookDto.getCategoryDto().getCategoryName())
                        .build())
                .subCategory(SubCategory.builder()
                        .id(bookDto.getSubCategoryDto().getId())
                        .subCategoryName(bookDto.getSubCategoryDto().getSubCategoryName())
                        .build())
                .build();
    }
}