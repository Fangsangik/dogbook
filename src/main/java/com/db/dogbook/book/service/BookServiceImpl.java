package com.db.dogbook.book.service;

import com.db.dogbook.book.bookDto.BookConverter;
import com.db.dogbook.book.bookDto.BookDto;
import com.db.dogbook.book.model.Book;
import com.db.dogbook.book.model.QBook;
import com.db.dogbook.book.repository.BookRepository;
import com.db.dogbook.category.converter.CategoryConverter;
import com.db.dogbook.category.converter.SubCategoryConverter;
import com.db.dogbook.category.domain.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final LikeCountService likeCountService;
    private final BookConverter bookConverter;
    private final JPAQueryFactory queryFactory;
    private final CategoryConverter categoryConverter;
    private final SubCategoryConverter subCategoryConverter;

    // 책 이름으로 검색
    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findByBookName(BookDto bookDto, Pageable pageable) {
        Page<Book> findBook = bookRepository.findByBookName(bookDto.getBookName(), pageable);
        if (findBook.isEmpty()) {
            log.warn("No book found with name {}", bookDto.getBookName());
        } else {
            log.info("Found book with name {}", bookDto.getBookName());
        }
        return findBook.map(bookConverter::toBookDto);
    }

    // 저자로 책 검색
    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findByAuthor(BookDto bookDto, Pageable pageable) {
        Page<Book> byAuthor = bookRepository.findByAuthor(bookDto.getAuthor(), pageable);
        if (byAuthor.isEmpty()) {
            log.warn("No book found with author {}", bookDto.getAuthor());
        } else {
            log.info("Found book with author {}", bookDto.getAuthor());
        }
        return byAuthor.map(bookConverter::toBookDto);
    }

    @Override
    @Transactional
    public BookDto create(BookDto bookDto) {
        validationOfBook(bookDto);
        // Book 생성
        Book book = Book.builder()
                .id(bookDto.getId())
                .bookName(bookDto.getBookName())
                .author(bookDto.getAuthor())
                .price(bookDto.getPrice())
                .blockDt(bookDto.getBlockDt())
                .thumb(bookDto.getThumb())
                .likeCnt(bookDto.getLikeCnt())
                .fileIdx(bookDto.getFileIdx())
                .saveDt(bookDto.getSaveDt())
                .category(categoryConverter.toCategory(bookDto.getCategoryDto())) // Category 설정
                .subCategory(subCategoryConverter.toSubCategory(bookDto.getSubCategoryDto()))
                .build();

        // SubCategory 설정

        Book savedBooks = bookRepository.save(book);
        return bookConverter.toBookDto(savedBooks);
    }

    @Override
    // 카테고리와 서브 카테고리로 책 검색
    @Transactional(readOnly = true)
    public List<BookDto> findByCategoryAndSubCategory(String categoryName, String subCategoryName) {
        QBook book = QBook.book;
        QCategory category = QCategory.category;
        QSubCategory subCategory = QSubCategory.subCategory;

        BooleanBuilder builder = new BooleanBuilder();
        if (categoryName != null && !categoryName.isEmpty()) {
            builder.and(category.categoryName.eq(categoryName));
        }
        if (subCategoryName != null && !subCategoryName.isEmpty()) {
            builder.and(subCategory.subCategoryName.eq(subCategoryName));
        }

        List<Book> books = queryFactory
                .selectFrom(book)
                .leftJoin(book.category, category)
                .leftJoin(book.subCategory, subCategory)
                .where(builder)
                .fetch();

        return books.stream()
                .map(bookConverter::toBookDto)
                .collect(Collectors.toList());
    }

    @Override
    // 책 정보 업데이트
    @Transactional
    public BookDto update(BookDto bookDto) {
        validationOfBook(bookDto);

        Book book = bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        if (bookDto.getId() == null) {
            throw new IllegalArgumentException("Book Id is null");
        }

        book.setCategory(categoryConverter.toCategory(bookDto.getCategoryDto()));
        book.setThumb(bookDto.getThumb());
        book.setFileIdx(bookDto.getFileIdx());
        book.setSaveDt(bookDto.getSaveDt());
        book.setBookName(bookDto.getBookName());
        book.setAuthor(bookDto.getAuthor());
        book.setPrice(bookDto.getPrice());
        book.setBlockDt(bookDto.getBlockDt());
        book.setSubCategory(subCategoryConverter.toSubCategory(bookDto.getSubCategoryDto()));

        if (bookDto.getLikeCnt() > book.getLikeCnt()) {
            likeCountService.increasedLikeCnt(bookDto.getId());
        } else if (bookDto.getLikeCnt() < book.getLikeCnt()) {
            likeCountService.decreasedLikeCnt(bookDto.getId());
        }
        book.setLikeCnt(bookDto.getLikeCnt());

        Book updatedBooks = bookRepository.save(book);

        return bookConverter.toBookDto(updatedBooks);
    }

    @Override
    // 책 삭제
    @Transactional
    public void deleteById(Long id) {
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
            bookRepository.deleteById(id);
        } catch (RuntimeException e) {
            log.error("Error deleting book", e);
            throw new RuntimeException("Failed to delete book", e);
        }
    }

    @Override
    // 책 가격 범위로 검색
    @Transactional(readOnly = true)
    public List<BookDto> findBooksByPriceRange(String bookName, Integer minPrice, Integer maxPrice) {
        QBook book = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();

        if (bookName != null) {
            builder.and(book.bookName.containsIgnoreCase(bookName));
        }

        if (minPrice != null) {
            builder.and(book.price.goe(minPrice));
        }

        if (maxPrice != null) {
            builder.and(book.price.loe(maxPrice));
        }

        List<Book> books = queryFactory
                .selectFrom(book)
                .where(builder)
                .fetch();

        return books.stream()
                .map(bookConverter::toBookDto)
                .collect(Collectors.toList());
    }

    private static void validationOfBook(BookDto bookDto) {
        if (bookDto.getId() == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }
        if (bookDto.getBookName() == null || bookDto.getBookName().isEmpty()) {
            throw new IllegalArgumentException("Book name cannot be null or empty");
        }
        if (bookDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Book price must be greater than zero");
        }
    }

    @Override
    // 주어진 카테고리, 서브카테고리, 책 이름으로 책 검색
    @Transactional(readOnly = true)
    public List<BookDto> findByCategorySubcategoryAndBookName(String categoryName, String subCategoryName, String bookName) {
        QBook book = QBook.book;
        QCategory category = QCategory.category;
        QSubCategory subCategory = QSubCategory.subCategory;
        BooleanBuilder builder = new BooleanBuilder();

        if (categoryName != null && !categoryName.isEmpty()) {
            builder.and(category.categoryName.eq(categoryName));
        }

        if (subCategoryName != null && !subCategoryName.isEmpty()) {
            builder.and(subCategory.subCategoryName.eq(subCategoryName));
        }

        if (bookName != null && !bookName.isEmpty()) {
            builder.and(book.bookName.eq(bookName));
        }

        List<Book> books = queryFactory
                .selectFrom(book)
                .leftJoin(book.category, category) // Book과 Category 조인
                .leftJoin(book.subCategory, subCategory)
                .where(builder)
                .fetch();

        return books.stream()
                .map(bookConverter::toBookDto)
                .collect(Collectors.toList());
    }

}
