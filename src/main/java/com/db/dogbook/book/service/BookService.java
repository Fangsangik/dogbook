package com.db.dogbook.book.service;

import com.db.dogbook.book.bookDto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    // 책 이름으로 검색 (페이징 처리)
    Page<BookDto> findByBookName(BookDto bookDto, Pageable pageable);

    // 저자로 검색 (페이징 처리)
    Page<BookDto> findByAuthor(BookDto bookDto, Pageable pageable);

    // 책 생성
    BookDto create(BookDto bookDto);

    // 카테고리와 서브 카테고리로 책 검색 (페이징 처리)
    Page<BookDto> findByCategoryAndSubCategory(String categoryName, String subCategoryName, Pageable pageable);

    // 책 업데이트
    BookDto update(BookDto bookDto);

    // 책 삭제
    void deleteById(Long id);

    // 가격 범위로 책 검색 (페이징 처리)
    Page<BookDto> findBooksByPriceRange(String bookName, Integer minPrice, Integer maxPrice, Pageable pageable);

    // 카테고리, 서브카테고리, 책 이름으로 검색 (페이징 처리)
    Page<BookDto> findByCategorySubcategoryAndBookName(String categoryName, String subCategoryName, String bookName, Pageable pageable);
}

