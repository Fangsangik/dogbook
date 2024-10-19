package com.db.dogbook.book.bookDto;

import com.db.dogbook.book.model.Book;
import com.db.dogbook.type.Category;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

    private final CategoryConverter categoryConverter;
    private final BookSubCategoryConverter bookSubCategoryConverter;
    private final SubCategoryConverter subCategoryConverter;

    public BookConverter(CategoryConverter categoryConverter, BookSubCategoryConverter bookSubCategoryConverter
            , SubCategoryConverter subCategoryConverter) {
        this.categoryConverter = categoryConverter;
        this.bookSubCategoryConverter = bookSubCategoryConverter;
        this.subCategoryConverter = subCategoryConverter;
    }

    // Book -> BookDto 변환
    public BookDto toBookDto(Book book) {
        // Book 엔티티의 BookSubCategory 리스트를 가져와서 이를 BookSubCategoryDto로 변환
        List<BookSubCategoryDto> bookSubCategoryDtos = new ArrayList<>(); // 변환된 BookSubCategoryDto 리스트

        // 기존 Book 엔티티에서 BookSubCategory 리스트를 반복하면서 BookSubCategoryDto로 변환
        for (BookSubCategory bookSubCategory : book.getBookSubCategories()) {
            // 각 BookSubCategory 객체를 BookSubCategoryDto 객체로 변환
            SubCategoryDto subCategoryDto = SubCategoryDto.builder()
                    .id(bookSubCategory.getSubCategory().getId()) // SubCategory의 ID 설정
                    .subCategoryName(bookSubCategory.getSubCategory().getSubCategoryName()) // SubCategory의 이름 설정
                    .build(); // SubCategoryDto 완성

            // BookSubCategoryDto 생성
            BookSubCategoryDto bookSubCategoryDto = BookSubCategoryDto.builder()
                    .subCategoryDto(subCategoryDto) // 변환된 SubCategoryDto 설정
                    .build(); // BookSubCategoryDto 완성

            // 변환된 BookSubCategoryDto를 리스트에 추가
            bookSubCategoryDtos.add(bookSubCategoryDto);
        }

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
