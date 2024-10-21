package com.db.dogbook.book.controller;

import com.db.dogbook.book.bookDto.BookDto;
import com.db.dogbook.book.service.BookServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book") // RESTful API 경로
@RequiredArgsConstructor
public class BookController {

    private final BookServiceImpl bookServiceImpl;

    // GET: 책 API 환영 메시지를 반환
    @GetMapping("/")
    public ResponseEntity<String> bookHome() {
        return ResponseEntity.ok("책 API에 오신 것을 환영합니다.");
    }

    // GET: 책 이름으로 검색한 결과를 반환 (페이징 적용)
    @GetMapping("/bookName")
    public ResponseEntity<?> bookName(@RequestParam String name, Pageable pageable) {
        try {
            BookDto bookDto = new BookDto();
            bookDto.setBookName(name);

            Page<BookDto> bookPage = bookServiceImpl.findByBookName(bookDto, pageable);
            return ResponseEntity.ok(bookPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("책 검색 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // GET: 저자 이름으로 검색한 결과를 반환 (페이징 적용)
    @GetMapping("/authorName")
    public ResponseEntity<?> authorName(@RequestParam String name, Pageable pageable) {
        try {
            BookDto bookDto = new BookDto();
            bookDto.setAuthor(name);

            Page<BookDto> bookPage = bookServiceImpl.findByAuthor(bookDto, pageable);
            return ResponseEntity.ok(bookPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("저자 이름으로 책 검색 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // GET: 가격 범위로 책 검색 (페이징 적용)
    @GetMapping("/books/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String bookName,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            Pageable pageable) {
        try {
            Page<BookDto> bookPage = bookServiceImpl.findBooksByPriceRange(bookName, minPrice, maxPrice, pageable);
            return ResponseEntity.ok(bookPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("가격 범위로 책 검색 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // GET: 카테고리와 서브 카테고리로 책 검색 (페이징 적용)
    @GetMapping("/category/books")
    public ResponseEntity<?> findByCategoryAndSubCategory(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String subCategoryName,
            Pageable pageable) {
        try {
            Page<BookDto> bookPage = bookServiceImpl.findByCategoryAndSubCategory(categoryName, subCategoryName, pageable);
            return ResponseEntity.ok(bookPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("카테고리 및 서브카테고리로 책 검색 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // GET: 카테고리, 서브 카테고리, 책 이름으로 검색 (페이징 적용)
    @GetMapping("/search")
    public ResponseEntity<?> findByCategorySubcategoryAndBookName(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String subCategoryName,
            @RequestParam(required = false) String bookName,
            Pageable pageable) {
        try {
            Page<BookDto> bookPage = bookServiceImpl.findByCategorySubcategoryAndBookName(categoryName, subCategoryName, bookName, pageable);
            return ResponseEntity.ok(bookPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("카테고리, 서브카테고리 및 책 이름으로 검색 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // POST: 책을 업로드
    @PostMapping("/uploadBook")
    public ResponseEntity<?> uploadBook(@RequestBody @Valid BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("책 정보를 유효성 검사하는 중 오류가 발생했습니다: " + bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            BookDto createBook = bookServiceImpl.create(bookDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("책 업로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // POST: 책 정보를 업데이트
    @PostMapping("/updateBook")
    public ResponseEntity<?> updateBook(@RequestBody @Valid BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("책 정보를 유효성 검사하는 중 오류가 발생했습니다: " + bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            BookDto updatedBook = bookServiceImpl.update(bookDto);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("책 정보 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // DELETE: 책을 삭제
    @DeleteMapping("/deleteBook")
    public ResponseEntity<?> deleteBook(@RequestParam Long id) {
        try {
            bookServiceImpl.deleteById(id);
            return ResponseEntity.ok("책이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("책 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
