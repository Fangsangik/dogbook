package com.db.dogbook.book.controller;

import com.db.dogbook.book.bookDto.BookDto;
import com.db.dogbook.book.service.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book") // RESTful API 경로
@RequiredArgsConstructor
public class BookController {

    private final BookServiceImpl bookServiceImpl;

    // GET: 책 목록 페이지로 이동하는 대신, 모든 책 정보를 JSON으로 반환
    @GetMapping("/")
    public ResponseEntity<String> bookHome() {
        return ResponseEntity.ok("Welcome to Book API");
    }

    // GET: 책 이름으로 검색한 결과를 반환
    @GetMapping("/bookName")
    public ResponseEntity<?> bookName(@RequestParam String name, Pageable pageable) {
        try {
            BookDto bookDto = new BookDto();
            bookDto.setBookName(name);

            Page<BookDto> bookPage = bookServiceImpl.findByBookName(bookDto, pageable);
            return ResponseEntity.ok(bookPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving books: " + e.getMessage());
        }
    }

    // GET: 저자 이름으로 검색한 결과를 반환
    @GetMapping("/authorName")
    public ResponseEntity<?> authorName(@RequestParam String name, Pageable pageable) {
        try {
            BookDto bookDto = new BookDto();
            bookDto.setAuthor(name);

            Page<BookDto> bookPage = bookServiceImpl.findByAuthor(bookDto, pageable);
            return ResponseEntity.ok(bookPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving books: " + e.getMessage());
        }
    }

    @GetMapping("/books/search")
    public List<BookDto> search(
            @RequestParam(required = false) String bookName,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice) {
        return bookServiceImpl.findBooksByPriceRange(bookName, minPrice, maxPrice);
    }

    @GetMapping("/category/books")
    public List<BookDto> findByCategoryAndSubCategory(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String subCategoryName
    ) {
        return bookServiceImpl.findByCategoryAndSubCategory(categoryName, subCategoryName);
    }

    // POST: 책을 업로드
    @PostMapping("/uploadBook")
    public ResponseEntity<?> uploadBook(@RequestBody BookDto bookDto) {
        try {
            BookDto createBook = bookServiceImpl.create(bookDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading book: " + e.getMessage());
        }
    }

    // PUT: 책 정보를 업데이트
    @PostMapping("/updateBook")
    public ResponseEntity<?> updateBook(@RequestBody BookDto bookDto) {
        try {
            BookDto updatedBook = bookServiceImpl.update(bookDto);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating book: " + e.getMessage());
        }
    }

    // DELETE: 책을 삭제
    @DeleteMapping("/deleteBook")
    public ResponseEntity<?> deleteBook(@RequestParam Long id) {
        try {
            bookServiceImpl.deleteById(id);
            return ResponseEntity.ok("Book deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting book: " + e.getMessage());
        }
    }
}
