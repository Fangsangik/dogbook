package com.db.dogbook.book.bookDto;

import com.db.dogbook.book.model.Book;
import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.domain.SubCategory;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

    public BookDto toBookDto(Book book) {
        BookDto.BookDtoBuilder bookDtoBuilder = BookDto.builder()
                .id(book.getId())
                .bookName(book.getBookName())
                .author(book.getAuthor())
                .price(book.getPrice());

        // Category와 SubCategory가 null인 경우를 체크
        if (book.getCategory() != null) {
            bookDtoBuilder.categoryDto(CategoryDto.builder()
                    .id(book.getCategory().getId())
                    .categoryName(book.getCategory().getCategoryName())
                    .build());
        }

        if (book.getSubCategory() != null) {
            bookDtoBuilder.subCategoryDto(SubCategoryDto.builder()
                    .id(book.getSubCategory().getId())
                    .subCategoryName(book.getSubCategory().getSubCategoryName())
                    .build());
        }

        return bookDtoBuilder.build();
    }

    public Book toBook(BookDto bookDto) {
        return Book.builder()
                .id(bookDto.getId())
                .bookName(bookDto.getBookName())
                .author(bookDto.getAuthor())
                .price(bookDto.getPrice())
                .category(bookDto.getCategoryDto() != null ?
                        Category.builder()
                                .id(bookDto.getCategoryDto().getId())
                                .categoryName(bookDto.getCategoryDto().getCategoryName())
                                .build() : null) // 선택적 카테고리
                .subCategory(bookDto.getSubCategoryDto() != null ?
                        SubCategory.builder()
                                .id(bookDto.getSubCategoryDto().getId())
                                .subCategoryName(bookDto.getSubCategoryDto().getSubCategoryName())
                                .build() : null) // 선택적 서브 카테고리
                .build();
    }
}
