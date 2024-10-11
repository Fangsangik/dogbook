package com.db.dogbook.book.service;

import com.db.dogbook.book.dto.bookDto.BookConverter;
import com.db.dogbook.book.dto.bookDto.BookDto;
import com.db.dogbook.book.model.Book;
import com.db.dogbook.book.model.QBook;
import com.db.dogbook.book.repository.BookRepository;
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
public class BookService {

    private final BookRepository bookRepository;
    private final LikeCountService likeCountService;
    private final BookConverter bookConverter;
    private final JPAQueryFactory queryFactory;


    //List?? Page??
    @Transactional(readOnly = true)
    public Page<BookDto> findByBookName(BookDto bookDto, Pageable pageable) {
        Page<Book> findBook = bookRepository.findByBookName(bookDto.getBookName(), pageable);
        if (findBook.isEmpty()) {
            log.warn("No book found with name {}", bookDto.getBookName());
        } else {
            log.info("Found book with name {}", bookDto.getBookName());
        }

        //convert
        return findBook.map(bookConverter::toBookEntity);
    }

    //List?? Page??
    @Transactional(readOnly = true)
    public Page<BookDto> findByAuthor(BookDto bookDto, Pageable pageable) {

        Page<Book> byAuthor = bookRepository.findByAuthor(bookDto.getAuthor(), pageable);
        if (byAuthor.isEmpty()) {
            log.warn("No book found with author {}", bookDto.getAuthor());
        } else {
            log.info("Found book with author {}", bookDto.getAuthor());
        }

        // 엔티티를 DTO로 변환
        return byAuthor.map(bookConverter::toBookEntity);
    }

    @Transactional
    public BookDto create(BookDto bookDto) {
        validationOfBook(bookDto);

        try {
            Book book = Book.builder()
                    .bookName(bookDto.getBookName())
                    .author(bookDto.getAuthor())
                    .price(bookDto.getPrice())
                    .blockDt(bookDto.getBlockDt())
                    .category(bookDto.getCategory())
                    .thumb(bookDto.getThumb())
                    .likeCnt(bookDto.getLikeCnt())
                    .fileIdx(bookDto.getFileIdx())
                    .saveDt(bookDto.getSaveDt())
                    .build();

            Book savedBook = bookRepository.save(book);

            //convert
            return bookConverter.toBookEntity(savedBook);

        } catch (RuntimeException e) {
            log.error("Error creating book", e);
            throw new RuntimeException("Failed to create book", e);
        }
    }


    @Transactional
    public BookDto update(BookDto bookDto) {
        validationOfBook(bookDto);

        try {
            Book updatedBook = bookRepository.findById(bookDto.getId())
                    .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookDto.getId()));

            //더티체킹
            //생성자를 이용한 값 설정 or 빌더 패턴 적용
            updatedBook.setBookName(bookDto.getBookName());
            updatedBook.setAuthor(bookDto.getAuthor());
            updatedBook.setPrice(bookDto.getPrice());
            updatedBook.setSaveDt(bookDto.getSaveDt());
            updatedBook.setCategory(bookDto.getCategory());
            updatedBook.setLikeCnt(bookDto.getLikeCnt());

            if (bookDto.getLikeCnt() > updatedBook.getLikeCnt()) {
                likeCountService.increasedLikeCnt(bookDto.getId());
            } else if (bookDto.getLikeCnt() < updatedBook.getLikeCnt()) {
                likeCountService.decreasedLikeCnt(bookDto.getId());
            }

            bookRepository.save(updatedBook);

            //convert
            return bookConverter.toBookEntity(updatedBook);
        } catch (RuntimeException e) {
            log.error("Error updating book", e);
            throw new RuntimeException("Failed to update book", e);
        }
    }

    //Id?? Dto로 삭제??
    @Transactional
    public void deleteById(Long id) {
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
            bookRepository.deleteById(id);
            log.info("Deleted book with ID: {}", id);
        } catch (RuntimeException e) {
            log.error("Error deleting book", e);
            throw new RuntimeException("Failed to delete book", e);
        }
    }

    @Transactional(readOnly = true)
    public List<BookDto> findBooksByPriceRange(String bookName, Integer minPrice, Integer maxPrice) {

        QBook book = QBook.book;
        //동적쿼리를 작성할때 사용되는 클래스
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
                .map(bookConverter::toBookEntity)
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
}
