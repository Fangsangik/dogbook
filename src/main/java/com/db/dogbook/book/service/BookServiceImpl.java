package com.db.dogbook.book.service;

import com.db.dogbook.book.bookDto.BookConverter;
import com.db.dogbook.book.bookDto.BookDto;
import com.db.dogbook.book.model.Book;
import com.db.dogbook.book.model.QBook;
import com.db.dogbook.book.repository.BookRepository;
import com.db.dogbook.category.converter.CategoryConverter;
import com.db.dogbook.category.converter.SubCategoryConverter;
import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.domain.QCategory;
import com.db.dogbook.category.domain.QSubCategory;
import com.db.dogbook.category.domain.SubCategory;
import com.db.dogbook.category.repository.CategoryRepository;
import com.db.dogbook.category.repository.SubCategoryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final BookConverter bookConverter;
    private final JPAQueryFactory queryFactory;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryConverter categoryConverter;
    private final SubCategoryConverter subCategoryConverter;

    @Override
    @Transactional
    public BookDto create(BookDto bookDto) {
        validationOfBook(bookDto);

        Category category = null;
        if (bookDto.getCategoryDto() != null) {
            if (bookDto.getCategoryDto().getId() != null) {
                // 이미 존재하는 카테고리 가져오기
                category = categoryRepository.findById(bookDto.getCategoryDto().getId())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
            } else {
                // 새 카테고리 생성
                category = categoryConverter.toCategory(bookDto.getCategoryDto());
                category = categoryRepository.save(category); // 카테고리 먼저 저장
            }
        }


        SubCategory subCategory = null;
        if (bookDto.getSubCategoryDto() != null) {
            subCategory = subCategoryConverter.toSubCategory(bookDto.getSubCategoryDto());
            if (subCategory.getId() == null) {
                subCategory.setCategory(category); // 서브 카테고리는 선택적 카테고리와 연관
                subCategory = subCategoryRepository.save(subCategory);
            }
        }

        Book book = Book.builder()
                .bookName(bookDto.getBookName())
                .author(bookDto.getAuthor())
                .price(bookDto.getPrice())
                .thumb(bookDto.getThumb())
                .likeCnt(bookDto.getLikeCnt() != null ? bookDto.getLikeCnt() : 0)
                .fileIdx(bookDto.getFileIdx())
                .category(category) // 선택적 관계 설정
                .subCategory(subCategory) // 선택적 관계 설정
                .build();

        Book savedBook = bookRepository.save(book);
        return bookConverter.toBookDto(savedBook);
    }

    @Override
    @Transactional
    public BookDto update(BookDto bookDto) {
        validationOfBook(bookDto);

        Book book = bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        // 카테고리 확인
        Category category = null;
        if (bookDto.getCategoryDto() != null && bookDto.getCategoryDto().getId() != null) {
            category = categoryRepository.findById(bookDto.getCategoryDto().getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        }

        // 서브 카테고리 확인
        SubCategory subCategory = null;
        if (bookDto.getSubCategoryDto() != null && bookDto.getSubCategoryDto().getId() != null) {
            subCategory = subCategoryRepository.findById(bookDto.getSubCategoryDto().getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서브카테고리입니다."));
        }

        // Book 객체 업데이트
        book.setBookName(bookDto.getBookName());
        book.setAuthor(bookDto.getAuthor());
        book.setPrice(bookDto.getPrice());
        book.setThumb(bookDto.getThumb());
        book.setFileIdx(bookDto.getFileIdx());
        book.setCategory(category); // 저장된 카테고리 설정
        book.setSubCategory(subCategory); // 저장된 서브 카테고리 설정

        Book updatedBook = bookRepository.save(book);
        return bookConverter.toBookDto(updatedBook);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findBooksByPriceRange(String bookName, Integer minPrice, Integer maxPrice, Pageable pageable) {
        QBook book = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();

        // 책 이름 필터링
        if (bookName != null && !bookName.isEmpty()) {
            builder.and(book.bookName.containsIgnoreCase(bookName));
        }

        // 최소 가격 필터링
        if (minPrice != null) {
            builder.and(book.price.goe(minPrice));
        }

        // 최대 가격 필터링
        if (maxPrice != null) {
            builder.and(book.price.loe(maxPrice));
        }

        // 책 검색 및 페이징 처리
        List<Book> books = queryFactory
                .selectFrom(book)
                .where(builder)
                .offset(pageable.getOffset())  // 페이징 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기만큼 제한
                .fetch();

        // 총 개수 계산
        long total = queryFactory
                .selectFrom(book)
                .where(builder)
                .fetchCount();

        // Book 엔티티를 BookDto로 변환
        List<BookDto> bookDtos = books.stream()
                .map(bookConverter::toBookDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookDtos, pageable, total);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findByCategorySubcategoryAndBookName(String categoryName, String subCategoryName, String bookName, Pageable pageable) {
        QBook book = QBook.book;
        QCategory category = QCategory.category;
        QSubCategory subCategory = QSubCategory.subCategory;

        BooleanBuilder builder = new BooleanBuilder();

        // 카테고리 이름 필터링
        if (categoryName != null && !categoryName.isEmpty()) {
            builder.and(category.categoryName.eq(categoryName));
        }

        // 서브카테고리 이름 필터링
        if (subCategoryName != null && !subCategoryName.isEmpty()) {
            builder.and(subCategory.subCategoryName.eq(subCategoryName));
        }

        // 책 이름 필터링
        if (bookName != null && !bookName.isEmpty()) {
            builder.and(book.bookName.containsIgnoreCase(bookName));
        }

        // 책 검색 및 페이징 처리
        List<Book> books = queryFactory
                .selectFrom(book)
                .leftJoin(book.category, category)
                .leftJoin(book.subCategory, subCategory)
                .where(builder)
                .offset(pageable.getOffset())  // 페이징 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기만큼 제한
                .fetch();

        // 총 개수 계산
        long total = queryFactory
                .selectFrom(book)
                .leftJoin(book.category, category)
                .leftJoin(book.subCategory, subCategory)
                .where(builder)
                .fetchCount();

        // Book 엔티티를 BookDto로 변환
        List<BookDto> bookDtos = books.stream()
                .map(bookConverter::toBookDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookDtos, pageable, total);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findByBookName(BookDto bookDto, Pageable pageable) {
        Page<Book> findBook = bookRepository.findByBookName(bookDto.getBookName(), pageable);
        return findBook.map(bookConverter::toBookDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findByAuthor(BookDto bookDto, Pageable pageable) {
        Page<Book> byAuthor = bookRepository.findByAuthor(bookDto.getAuthor(), pageable);
        return byAuthor.map(bookConverter::toBookDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findByCategoryAndSubCategory(String categoryName, String subCategoryName, Pageable pageable) {
        // QueryDSL을 사용한 카테고리 및 서브카테고리 검색
        BooleanBuilder builder = new BooleanBuilder();
        if (categoryName != null && !categoryName.isEmpty()) {
            builder.and(QCategory.category.categoryName.eq(categoryName));
        }
        if (subCategoryName != null && !subCategoryName.isEmpty()) {
            builder.and(QSubCategory.subCategory.subCategoryName.eq(subCategoryName));
        }

        List<Book> books = queryFactory
                .selectFrom(QBook.book)
                .leftJoin(QBook.book.category, QCategory.category)
                .leftJoin(QBook.book.subCategory, QSubCategory.subCategory)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BookDto> bookDtos = books.stream()
                .map(bookConverter::toBookDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookDtos, pageable, bookDtos.size());
    }

    private void validationOfBook(BookDto bookDto) {
        if (bookDto.getBookName() == null || bookDto.getBookName().isEmpty()) {
            throw new IllegalArgumentException("Book name cannot be null or empty");
        }
        if (bookDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Book price must be greater than zero");
        }
        // 카테고리와 서브카테고리가 모두 null인 경우 에러 처리
        if (bookDto.getCategoryDto() == null && bookDto.getSubCategoryDto() == null) {
            throw new IllegalArgumentException("At least one of Category or SubCategory must be provided");
        }
    }
}
