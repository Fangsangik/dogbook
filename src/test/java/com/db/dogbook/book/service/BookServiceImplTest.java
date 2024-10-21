package com.db.dogbook.book.service;

import com.db.dogbook.book.bookDto.BookDto;
import com.db.dogbook.book.repository.BookRepository;
import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.repository.CategoryRepository;
import com.db.dogbook.category.repository.SubCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookServiceImpl;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    private BookDto bookDto;
    private CategoryDto categoryDto;
    private SubCategoryDto subCategoryDto;

    @BeforeEach
    @Transactional
    void setUp() {
        // 각 테스트 전에 DB 초기화 및 데이터 설정
        bookRepository.deleteAll();
        subCategoryRepository.deleteAll();
        categoryRepository.deleteAll();

        categoryDto = CategoryDto.builder()
                .categoryName("Test Category")
                .build();

        subCategoryDto = SubCategoryDto.builder()
                .subCategoryName("Test SubCategory")
                .categoryDto(categoryDto)
                .build();

        bookDto = BookDto.builder()
                .bookName("Test Book")
                .author("Test Author")
                .price(10000)
                .likeCnt(5)
                .categoryDto(categoryDto)
                .subCategoryDto(subCategoryDto)
                .build();
    }

    @Test
    @Transactional
    void create() {
        BookDto bookDto = bookServiceImpl.create(this.bookDto);

    }
    @Test
    @Transactional
    void findByBookName() {

    }

    @Test
    @Transactional
    void findByAuthor() {

    }


    @Test
    @Transactional
    void findByCategoryAndSubCategory() {

    }

    @Test
    @Transactional
    void findByCategorySubcategoryAndBookName() {

    }

    @Test
    @Transactional
    void update() {
    }

    @Test
    @Transactional
    void deleteById() {

    }

    @Test
    @Transactional
    void findBooksByPriceRange() {

    }
}
