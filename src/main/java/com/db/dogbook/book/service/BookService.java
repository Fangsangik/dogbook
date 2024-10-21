package com.db.dogbook.book.service;

import com.db.dogbook.book.bookDto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    Page<BookDto> findByBookName(BookDto bookDto, Pageable pageable);

    Page<BookDto> findByAuthor(BookDto bookDto, Pageable pageable);

    BookDto create(BookDto bookDto);

    List<BookDto> findByCategoryAndSubCategory(String categoryName, String subCategoryName);

    BookDto update(BookDto bookDto);

    void deleteById(Long id);

    List<BookDto> findBooksByPriceRange(String bookName, Integer minPrice, Integer maxPrice);

    List<BookDto> findByCategorySubcategoryAndBookName(String categoryName, String subCategoryName, String bookName);
}
