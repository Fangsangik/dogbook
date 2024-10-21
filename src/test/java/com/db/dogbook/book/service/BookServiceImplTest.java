package com.db.dogbook.book.service;

import com.db.dogbook.book.bookDto.BookConverter;
import com.db.dogbook.book.bookDto.BookDto;
import com.db.dogbook.book.repository.BookRepository;
import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.converter.CategoryConverter;
import com.db.dogbook.category.converter.SubCategoryConverter;
import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.domain.SubCategory;
import com.db.dogbook.category.repository.CategoryRepository;
import com.db.dogbook.category.repository.SubCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


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

    @Autowired
    private CategoryConverter categoryConverter;

    @Autowired
    private BookConverter bookConverter;

    @Autowired
    private SubCategoryConverter subCategoryConverter;

    private BookDto bookDto;
    private CategoryDto categoryDto;
    private SubCategoryDto subCategoryDto;

    @BeforeEach
    @Transactional
    void setUp() {
        // DB 초기화
        bookRepository.deleteAll();
        subCategoryRepository.deleteAll();
        categoryRepository.deleteAll();

        // Category 저장
        categoryDto = CategoryDto.builder()
                .categoryName("Test Category")
                .build();
        Category savedCategory = categoryRepository.save(categoryConverter.toCategory(categoryDto));

        // SubCategory 저장
        subCategoryDto = SubCategoryDto.builder()
                .subCategoryName("Test SubCategory")
                .categoryDto(categoryConverter.toCategoryDto(savedCategory))  // CategoryDto 참조
                .build();
        SubCategory savedSubCategory = subCategoryRepository.save(subCategoryConverter.toSubCategory(subCategoryDto));

        // BookDto 설정
        bookDto = BookDto.builder()
                .bookName("Test Book")
                .author("Test Author")
                .price(10000)
                .categoryDto(categoryConverter.toCategoryDto(savedCategory))  // 저장된 CategoryDto 참조
                .subCategoryDto(subCategoryConverter.toSubCategoryDto(savedSubCategory))  // 저장된 SubCategoryDto 참조
                .likeCnt(0)  // 기본 likeCnt 설정
                .build();

        bookRepository.save(bookConverter.toBook(bookDto));
    }

    @Test
    @Transactional
    void create() {
        BookDto createdBook = bookServiceImpl.create(this.bookDto);
        assertNotNull(createdBook);
        assertEquals("Test Book", createdBook.getBookName());
        assertEquals("Test Author", createdBook.getAuthor());
    }

    @Test
    @Transactional
    void findByBookName() {
        bookRepository.deleteAll();
        bookServiceImpl.create(this.bookDto);
        var result = bookServiceImpl.findByBookName(bookDto, Pageable.unpaged());
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Book", result.getContent().get(0).getBookName());
    }

    @Test
    @Transactional
    void findByAuthor() {
        bookRepository.deleteAll();
        bookServiceImpl.create(this.bookDto);
        var result = bookServiceImpl.findByAuthor(bookDto, Pageable.unpaged());
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Author", result.getContent().get(0).getAuthor());
    }

    @Test
    @Transactional
    void findByCategoryAndSubCategory() {
        bookRepository.deleteAll();
        bookServiceImpl.create(this.bookDto);

        Pageable pageable = PageRequest.of(0, 10);  // 0페이지에서 10개 아이템 반환

        Page<BookDto> result = bookServiceImpl.findByCategoryAndSubCategory("Test Category", "Test SubCategory", pageable);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Book", result.getContent().get(0).getBookName());
    }

    @Test
    @Transactional
    void findByCategorySubcategoryAndBookName() {
        bookRepository.deleteAll();
        bookServiceImpl.create(this.bookDto);

        // Pageable 설정
        Pageable pageable = PageRequest.of(0, 10);  // 0페이지에서 10개 아이템 반환

        Page<BookDto> result = bookServiceImpl.findByCategorySubcategoryAndBookName("Test Category", "Test SubCategory", "Test Book", pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Book", result.getContent().get(0).getBookName());
    }

    @Test
    @Transactional
    void update() {
        bookRepository.deleteAll();
        BookDto createdBook = bookServiceImpl.create(this.bookDto);
        createdBook.setPrice(15000); // 가격 업데이트
        createdBook.setCategoryDto(categoryDto);
        createdBook.setSubCategoryDto(subCategoryDto);
        createdBook.setLikeCnt(0);

        BookDto updatedBook = bookServiceImpl.update(createdBook);
        assertEquals(15000, updatedBook.getPrice());
    }

    @Test
    @Transactional
    void deleteById() {
        BookDto createdBook = bookServiceImpl.create(this.bookDto);
        Long bookId = createdBook.getId();

        bookServiceImpl.deleteById(bookId);
        assertThrows(RuntimeException.class, () -> bookServiceImpl.deleteById(bookId));
    }

    @Test
    @Transactional
    void findBooksByPriceRange() {
        bookRepository.deleteAll();
        bookServiceImpl.create(this.bookDto);

        // Pageable 설정
        Pageable pageable = PageRequest.of(0, 10);  // 0페이지에서 10개 아이템 반환

        Page<BookDto> result = bookServiceImpl.findBooksByPriceRange("Test Book", 5000, 20000, pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Book", result.getContent().get(0).getBookName());
    }

}
