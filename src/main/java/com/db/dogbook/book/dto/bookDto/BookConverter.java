package com.db.dogbook.book.dto.bookDto;

import com.db.dogbook.book.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

    public BookDto toBookEntity(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .bookName(book.getBookName())
                .author(book.getAuthor())
                .price(book.getPrice())
                .blockDt(book.getBlockDt())
                .category(book.getCategory())
                .thumb(book.getThumb())
                .likeCnt(book.getLikeCnt())
                .fileIdx(book.getFileIdx())
                .saveDt(book.getSaveDt())
                .build();
    }
}
