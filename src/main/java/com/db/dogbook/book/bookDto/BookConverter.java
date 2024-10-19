package com.db.dogbook.book.bookDto;

import com.db.dogbook.book.model.Book;
import com.db.dogbook.category.categoryDto.BookSubCategoryDto;
import com.db.dogbook.category.converter.BookSubCategoryConverter;
import com.db.dogbook.category.converter.CategoryConverter;
import com.db.dogbook.category.converter.SubCategoryConverter;
import com.db.dogbook.category.domain.BookSubCategory;
import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.domain.SubCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookConverter {

    private final CategoryConverter categoryConverter;
    private final BookSubCategoryConverter bookSubCategoryConverter;
    private final SubCategoryConverter subCategoryConverter;

    public BookConverter(CategoryConverter categoryConverter, BookSubCategoryConverter bookSubCategoryConverter
    ,SubCategoryConverter subCategoryConverter) {
        this.categoryConverter = categoryConverter;
        this.bookSubCategoryConverter = bookSubCategoryConverter;
        this.subCategoryConverter = subCategoryConverter;
    }

    public BookDto toBookDto(Book book) {
        List<BookSubCategoryDto> bookSubCategoryDtos = new ArrayList<>();
        if (book.getBookSubCategories() != null) {
            for (BookSubCategory bookSubCategory : book.getBookSubCategories()) {
                BookSubCategoryDto bookSubCategoryDto = bookSubCategoryConverter.toBookSubCategoryDto(bookSubCategory);
                bookSubCategoryDtos.add(bookSubCategoryDto);
            }
        }

        return BookDto.builder()
                .id(book.getId())
                .bookName(book.getBookName())
                .author(book.getAuthor())
                .price(book.getPrice())
                .fileIdx(book.getFileIdx())
                .thumb(book.getThumb())
                .userId(book.getUserId())
                .likeCnt(book.getLikeCnt())
                .categoryDto(categoryConverter.toCategoryDto(book.getCategory()))
                .bookSubCategoryDtos(bookSubCategoryDtos)
                .blockYn(book.isBlockYn())
                .blockDt(book.getBlockDt())
                .saveDt(book.getSaveDt())
                .updtDt(book.getUpdtDt())
                .build();
    }

    public Book toBook(BookDto bookDto) {
        // Book 빌더를 사용하여 BookDto를 Book으로 변환
        Book book = Book.builder()
                .bookName(bookDto.getBookName())
                .author(bookDto.getAuthor())
                .price(bookDto.getPrice())
                .blockDt(bookDto.getBlockDt())
                .thumb(bookDto.getThumb())
                .likeCnt(bookDto.getLikeCnt())
                .fileIdx(bookDto.getFileIdx())
                .saveDt(bookDto.getSaveDt())
                .build();

        // CategoryDto -> Category 변환
        if (bookDto.getCategoryDto() != null) {
            Category category = categoryConverter.toCategory(bookDto.getCategoryDto());
            book.setCategory(category);
        }

        // SubCategoryDtos -> SubCategories 변환
        if (bookDto.getBookSubCategoryDtos() != null && !bookDto.getBookSubCategoryDtos().isEmpty()) {
            List<BookSubCategory> bookSubCategories = new ArrayList<>();
            for (BookSubCategoryDto bookSubCategoryDto : bookDto.getBookSubCategoryDtos()) {
                if (bookSubCategoryDto.getSubCategoryDto() != null) {
                    SubCategory subCategory = subCategoryConverter.toSubCategory(bookSubCategoryDto.getSubCategoryDto());
                    BookSubCategory bookSubCategory = BookSubCategory.builder()
                            .book(book)
                            .subCategory(subCategory)
                            .build();
                    bookSubCategories.add(bookSubCategory);
                }
            }
            book.setBookSubCategories(bookSubCategories);
        }

        return book;
    }

}