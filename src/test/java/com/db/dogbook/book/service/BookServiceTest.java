package com.db.dogbook.book.service;

import com.db.dogbook.book.bookDto.BookConverter;
import com.db.dogbook.book.bookDto.BookDto;
import com.db.dogbook.book.model.Book;
import com.db.dogbook.book.repository.BookRepository;

import com.db.dogbook.category.categoryDto.BookSubCategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.converter.CategoryConverter;
import com.db.dogbook.category.domain.BookSubCategory;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

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

    private BookDto bookDto;

    @BeforeEach
    @Transactional
    void setUp() {
        // 기존 데이터 삭제
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
        subCategoryRepository.deleteAll();

        // 카테고리 및 서브 카테고리 생성 및 저장
        Category category = categoryRepository.save(
                Category.builder()
                        .categoryName("Backend")
                        .build()
        );

        SubCategory subCategory = subCategoryRepository.save(
                SubCategory.builder()
                        .subCategoryName("Java")
                        .category(category)  // 서브 카테고리에 카테고리 설정
                        .build()
        );

        // BookSubCategory 생성
        BookSubCategory bookSubCategory = BookSubCategory.builder()
                .subCategory(subCategory)
                .build();

        // Book 엔티티 생성 및 서브 카테고리 연결
        List<BookSubCategory> bookSubCategories = new ArrayList<>();
        bookSubCategories.add(bookSubCategory);

        Book book = Book.builder()
                .bookName("토비의 스프링")
                .author("이일빈")
                .price(10000)
                .likeCnt(1000)
                .category(category)  // 책에 카테고리 설정
                .bookSubCategories(bookSubCategories)  // 서브 카테고리 연결
                .build();

        // BookSubCategory에도 Book 연결 (수정된 부분: 먼저 연결하고 책을 저장)
        bookSubCategory.setBook(book);

        // 책 저장
        book = bookRepository.save(book);

        // 저장된 책을 DTO로 변환
        bookDto = bookConverter.toBookDto(book);

        // 중요: 서브 카테고리 DTO에 CategoryDto 설정
        SubCategoryDto subCategoryDto = bookDto.getBookSubCategoryDtos().get(0).getSubCategoryDto();
        subCategoryDto.setCategoryDto(categoryConverter.toCategoryDto(category));
        subCategoryDto.setId(subCategory.getId());

        // 확인: 서브 카테고리와 카테고리 저장 확인 (추가된 부분)
        assertNotNull(bookSubCategory.getSubCategory(), "SubCategory should not be null");
        assertEquals("Java", bookSubCategory.getSubCategory().getSubCategoryName());
    }

    @Test
    void findByBookName() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookDto> byBookName = bookService.findByBookName(bookDto, pageable);
        assertNotNull(byBookName);
        assertEquals(1, byBookName.getTotalElements());
        assertThat(byBookName.getContent().get(0).getBookName()).isEqualTo("토비의 스프링");
    }

    @Test
    void findByAuthor() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookDto> byAuthor = bookService.findByAuthor(bookDto, pageable);
        assertNotNull(byAuthor);
        assertEquals(1, byAuthor.getTotalElements());
        assertThat(byAuthor.getContent().get(0).getAuthor()).isEqualTo("이일빈");
    }

    @Test
    void create() {
        // 책 생성
        BookDto createdBook = bookService.create(bookDto);

        // 생성된 책 검증
        assertNotNull(createdBook);
        assertThat(createdBook.getBookName()).isEqualTo(bookDto.getBookName());
        assertThat(createdBook.getAuthor()).isEqualTo(bookDto.getAuthor());
        assertThat(createdBook.getPrice()).isEqualTo(bookDto.getPrice());
        assertNotNull(createdBook.getCategoryDto());  // 카테고리가 null이 아닌지 확인

        // 서브 카테고리 검증
        assertNotNull(createdBook.getBookSubCategoryDtos(), "BookSubCategoryDtos should not be null");
        assertFalse(createdBook.getBookSubCategoryDtos().isEmpty(), "BookSubCategoryDtos should not be empty");

        // 서브 카테고리 아이템 검증
        assertThat(createdBook.getBookSubCategoryDtos().get(0).getSubCategoryDto().getId())
                .isEqualTo(bookDto.getBookSubCategoryDtos().get(0).getSubCategoryDto().getId());
        assertThat(createdBook.getBookSubCategoryDtos().get(0).getSubCategoryDto().getSubCategoryName())
                .isEqualTo(bookDto.getBookSubCategoryDtos().get(0).getSubCategoryDto().getSubCategoryName());
    }

    @Test
    void findByCategoryAndSubCategory() {
        // 책 생성
        BookDto createdBook = bookService.create(bookDto);

        // 서브 카테고리가 추가되었는지 확인
        assertFalse(createdBook.getBookSubCategoryDtos().isEmpty(), "서브 카테고리가 비어 있으면 안 됩니다");

        // 책 검색
        List<BookDto> books = bookService.findByCategoryAndSubCategory("Backend", "Java");

        // 검증
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertThat(books.get(0).getCategoryDto().getCategoryName()).isEqualTo("Backend");
        assertThat(books.get(0).getBookSubCategoryDtos().get(0).getSubCategoryDto().getSubCategoryName())
                .isEqualTo("Java"); // 서브 카테고리 확인
    }

    @Test
    void findByCategorySubcategoryAndBookName() {
        // 먼저 책을 생성합니다.
        BookDto createdBook = bookService.create(bookDto);

        // 생성한 책이 제대로 검색되는지 확인합니다. (카테고리, 서브 카테고리, 책 이름으로 검색)
        List<BookDto> books = bookService.findByCategorySubcategoryAndBookName(
                createdBook.getCategoryDto().getCategoryName(), // "Backend" 카테고리
                createdBook.getBookSubCategoryDtos().get(0).getSubCategoryDto().getSubCategoryName(), // "Java" 서브 카테고리
                createdBook.getBookName() // "토비의 스프링" 책 이름
        );

        // 검증: 책 리스트가 null이 아니고 비어 있지 않은지 확인
        assertNotNull(books, "책 리스트가 null이면 안 됩니다.");
        assertFalse(books.isEmpty(), "책 리스트가 비어 있으면 안 됩니다.");

        // 검색된 첫 번째 책에 대해 카테고리, 서브 카테고리, 책 이름이 맞는지 확인합니다.
        BookDto foundBook = books.get(0);
        assertThat(foundBook.getCategoryDto().getCategoryName()).isEqualTo("Backend"); // 카테고리 확인
        assertThat(foundBook.getBookSubCategoryDtos().get(0).getSubCategoryDto().getSubCategoryName())
                .isEqualTo("Java"); // 서브 카테고리 확인
        assertThat(foundBook.getBookName()).isEqualTo("토비의 스프링"); // 책 이름 확인
    }

    @Test
    void update() {
        BookDto created = bookService.create(bookDto);

        // 업데이트
        created.setBookName("JumpToJava");
        created.setPrice(12000);

        BookDto updated = bookService.update(created);
        assertNotNull(updated);
        assertThat(updated.getBookName()).isEqualTo("JumpToJava");
        assertThat(updated.getPrice()).isEqualTo(12000);
    }

    @Test
    void deleteById() {
        // 책 생성
        BookDto createdBook = bookService.create(bookDto);

        // 책 삭제
        bookService.deleteById(createdBook.getId());

        // 삭제된 책에 접근 시 예외 발생 여부 확인
        assertThrows(RuntimeException.class, () -> {
            bookService.deleteById(createdBook.getId());
        });
    }

    @Test
    void findBooksByPriceRange() {
        List<BookDto> books = bookService.findBooksByPriceRange("토비의 스프링", 5000, 15000);

        // 검증
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertThat(books.get(0).getBookName()).isEqualTo("토비의 스프링");
        assertThat(books.get(0).getPrice()).isBetween(5000, 15000);
    }
}
