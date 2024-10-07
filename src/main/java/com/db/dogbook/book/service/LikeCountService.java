package com.db.dogbook.book.service;

import com.db.dogbook.book.model.Book;
import com.db.dogbook.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeCountService {

    private final BookRepository bookRepository;

    @Transactional
    public void increasedLikeCnt(Long bookId) {
        Book likedBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        likedBook.setLikeCnt(likedBook.getLikeCnt() + 1);
        bookRepository.save(likedBook);
    }

    @Transactional
    public void decreasedLikeCnt(Long bookId) {
        Book unlikeBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));


        if (unlikeBook.getLikeCnt() > 0) {
            unlikeBook.setLikeCnt(unlikeBook.getLikeCnt() - 1);
            bookRepository.save(unlikeBook);
        }
    }
}
