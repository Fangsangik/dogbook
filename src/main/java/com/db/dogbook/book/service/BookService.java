package com.db.dogbook.book.service;

import com.db.dogbook.book.bookDto.BookConverter;
import com.db.dogbook.book.bookDto.BookDto;
import com.db.dogbook.book.model.Book;
import com.db.dogbook.book.model.QBook;
import com.db.dogbook.book.repository.BookRepository;
import com.db.dogbook.category.categoryDto.BookSubCategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.converter.BookSubCategoryConverter;
import com.db.dogbook.category.converter.CategoryConverter;
import com.db.dogbook.category.domain.*;
import com.db.dogbook.category.repository.SubCategoryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LikeCountService likeCountService;
    private final BookConverter bookConverter;
    private final JPAQueryFactory queryFactory;
    private final CategoryConverter categoryConverter;
    private final BookSubCategoryConverter bookSubCategoryConverter;
    private final SubCategoryRepository subCategoryRepository;

    // 책 이름으로 검색
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

    @Transactional
    public BookDto create(BookDto bookDto) {
        validationOfBook(bookDto);
        try {
            // Book 생성
            Book book = Book.builder()
                    .bookName(bookDto.getBookName())
                    .author(bookDto.getAuthor())
                    .price(bookDto.getPrice())
                    .blockDt(bookDto.getBlockDt())
                    .thumb(bookDto.getThumb())
                    .likeCnt(bookDto.getLikeCnt())
                    .fileIdx(bookDto.getFileIdx())
                    .saveDt(bookDto.getSaveDt())
                    .category(categoryConverter.toCategory(bookDto.getCategoryDto())) // Category 설정
                    .build();

            // SubCategory 설정
            List<BookSubCategory> bookSubCategories = new ArrayList<>();
            for (BookSubCategoryDto bookSubCategoryDto : bookDto.getBookSubCategoryDtos()) {
                SubCategoryDto subCategoryDto = bookSubCategoryDto.getSubCategoryDto();

                // SubCategoryDto에 CategoryDto가 있는지 확인하고 설정
                if (subCategoryDto.getCategoryDto() == null) {
                    throw new RuntimeException("CategoryDto is missing in SubCategoryDto");
                }

                SubCategory subCategory = subCategoryRepository.findById(subCategoryDto.getId())
                        .orElseThrow(() -> new RuntimeException("SubCategory not found"));

                bookSubCategories.add(BookSubCategory.builder()
                        .book(book)
                        .subCategory(subCategory)
                        .build());
            }

            // 책에 서브 카테고리 연결
            book.setBookSubCategories(bookSubCategories);

            // Book 저장
            Book savedBook = bookRepository.save(book);

            // Book -> BookDto 변환 후 반환
            return bookConverter.toBookDto(savedBook);
        } catch (RuntimeException e) {
            log.error("Error creating book", e);
            throw new RuntimeException("Failed to create book", e);
        }
    }

    // 카테고리와 서브 카테고리로 책 검색
    @Transactional(readOnly = true)
    public List<BookDto> findByCategoryAndSubCategory(String categoryName, String subCategoryName) {
        QBook book = QBook.book;
        QCategory category = QCategory.category;
        QBookSubCategory bookSubCategory = QBookSubCategory.bookSubCategory;
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
                .leftJoin(book.bookSubCategories, bookSubCategory)
                .leftJoin(bookSubCategory.subCategory, subCategory)
                .where(builder)
                .fetch();

        return books.stream()
                .map(bookConverter::toBookDto)
                .collect(Collectors.toList());
    }

    // 책 정보 업데이트
    @Transactional
    public BookDto update(BookDto bookDto) {
        validationOfBook(bookDto);
        try {
            Book updatedBook = bookRepository.findById(bookDto.getId())
                    .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookDto.getId()));

            updatedBook.setBookName(bookDto.getBookName());
            updatedBook.setAuthor(bookDto.getAuthor());
            updatedBook.setPrice(bookDto.getPrice());
            updatedBook.setSaveDt(bookDto.getSaveDt());

            if (bookDto.getCategoryDto() != null) {
                updatedBook.setCategory(categoryConverter.toCategory(bookDto.getCategoryDto()));
            }

            if (bookDto.getLikeCnt() > updatedBook.getLikeCnt()) {
                likeCountService.increasedLikeCnt(bookDto.getId());
            } else if (bookDto.getLikeCnt() < updatedBook.getLikeCnt()) {
                likeCountService.decreasedLikeCnt(bookDto.getId());
            }
            updatedBook.setLikeCnt(bookDto.getLikeCnt());

            updateBookSubCategories(bookDto, updatedBook);
            bookRepository.save(updatedBook);

            return bookConverter.toBookDto(updatedBook);
        } catch (RuntimeException e) {
            log.error("Error updating book", e);
            throw new RuntimeException("Failed to update book", e);
        }
    }

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

    // 주어진 카테고리, 서브카테고리, 책 이름으로 책 검색
    @Transactional(readOnly = true)
    public List<BookDto> findByCategorySubcategoryAndBookName(String categoryName, String subCategoryName, String bookName) {
        QBook book = QBook.book;
        QCategory category = QCategory.category;
        QBookSubCategory bookSubCategory = QBookSubCategory.bookSubCategory;
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
                .leftJoin(book.bookSubCategories, bookSubCategory) // Book과 BookSubCategory 조인
                .leftJoin(bookSubCategory.subCategory, subCategory) // BookSubCategory와 SubCategory 조인
                .where(builder)
                .fetch();

        return books.stream()
                .map(bookConverter::toBookDto)
                .collect(Collectors.toList());
    }


    private void updateBookSubCategories(BookDto bookDto, Book book) {
        // bookDto의 서브 카테고리 리스트가 존재하고, 비어 있지 않을 경우에만 업데이트 수행
        if (bookDto.getBookSubCategoryDtos() != null && !bookDto.getBookSubCategoryDtos().isEmpty()) {
            // 현재 Book에 연결된 기존 서브 카테고리 리스트를 가져옴
            List<BookSubCategory> existingSubCategories = book.getBookSubCategories();

            // 새롭게 추가될 서브 카테고리 리스트를 생성
            List<BookSubCategory> newSubCategories = new ArrayList<>();
            for (BookSubCategoryDto bookSubCategoryDto : bookDto.getBookSubCategoryDtos()) {
                // SubCategory ID를 사용해 서브 카테고리 엔티티를 조회
                SubCategory subCategory = subCategoryRepository.findById(bookSubCategoryDto.getSubCategoryDto().getId())
                        .orElseThrow(() -> new RuntimeException("SubCategory not found"));

                // BookSubCategory 엔티티 생성 (Book과 SubCategory 연결)
                BookSubCategory newSubCategory = BookSubCategory.builder()
                        .book(book) // 해당 Book과 연결
                        .subCategory(subCategory) // 조회된 SubCategory와 연결
                        .build(); // BookSubCategory 객체 반환

                newSubCategories.add(newSubCategory); // 새로운 리스트에 추가
            }

            // 기존 서브 카테고리 리스트에서 새 리스트에 없는 항목들을 제거 (제거 기준: SubCategory ID 비교)
            List<BookSubCategory> subCategoriesToRemove = new ArrayList<>();
            for (BookSubCategory existingSubCategory : existingSubCategories) {
                boolean found = false;
                for (BookSubCategory newSubCategory : newSubCategories) {
                    if (newSubCategory.getSubCategory().getId().equals(existingSubCategory.getSubCategory().getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    subCategoriesToRemove.add(existingSubCategory); // 제거할 항목으로 추가
                }
            }
            existingSubCategories.removeAll(subCategoriesToRemove); // 제거할 항목들 삭제

            // 새로운 서브 카테고리 리스트에서 기존 리스트에 없는 항목들을 추가
            for (BookSubCategory newSubCategory : newSubCategories) {
                boolean exists = false;
                for (BookSubCategory existingSubCategory : existingSubCategories) {
                    if (existingSubCategory.getSubCategory().getId().equals(newSubCategory.getSubCategory().getId())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    existingSubCategories.add(newSubCategory); // 기존 리스트에 새로 추가된 서브 카테고리 추가
                }
            }

            // 업데이트된 서브 카테고리 리스트를 Book 엔티티에 설정
            book.setBookSubCategories(existingSubCategories);
        } else {
            // 서브 카테고리가 없을 경우 빈 리스트로 설정하여 고아 처리
            book.setBookSubCategories(Collections.emptyList());
        }
    }

}
