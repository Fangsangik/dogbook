package com.db.dogbook.book.repository;

import com.db.dogbook.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByBookName(String name, Pageable pageable);

    Page<Book> findByAuthor(String name, Pageable pageable);

    Optional<Book> findByBookName(String bookName);
}
