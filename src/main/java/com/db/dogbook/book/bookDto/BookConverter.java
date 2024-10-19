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
                .categoryDto(categoryConverter.toCategoryDto(book.getCategory()))
                .bookSubCategoryDtos(bookSubCategoryDtos) // 서브 카테고리 DTO 설정
                .build();
    }


    // BookDto -> Book 변환
    public Book toBook(BookDto bookDto) {
        // Book 엔티티를 변환하기 위해 BookSubCategory 리스트를 생성
        List<BookSubCategory> bookSubCategories = new ArrayList<>(); // 변환된 BookSubCategory 리스트

        // bookDto의 BookSubCategoryDto 리스트를 반복하면서 각 BookSubCategoryDto를 BookSubCategory로 변환
        for (BookSubCategoryDto bookSubCategoryDto : bookDto.getBookSubCategoryDtos()) {
            SubCategory subCategory = null;

            // BookSubCategoryDto에 SubCategoryDto가 존재하면, 이를 SubCategory 엔티티로 변환
            if (bookSubCategoryDto.getSubCategoryDto() != null) {
                subCategory = subCategoryConverter.toSubCategory(bookSubCategoryDto.getSubCategoryDto());
            }

            // BookSubCategoryDto를 BookSubCategory로 변환
            BookSubCategory bookSubCategory = bookSubCategoryConverter.toBookSubCategory(bookSubCategoryDto, subCategory);

            // 변환된 BookSubCategory를 리스트에 추가
            bookSubCategories.add(bookSubCategory);
        }

        // CategoryDto가 존재하면, 이를 Category 엔티티로 변환
        Category category = null;
        if (bookDto.getCategoryDto() != null) {
            category = categoryConverter.toCategory(bookDto.getCategoryDto());
        }

        return Book.builder()
                .id(bookDto.getId())
                .bookName(bookDto.getBookName())
                .author(bookDto.getAuthor())
                .price(bookDto.getPrice())
                .fileIdx(bookDto.getFileIdx())
                .thumb(bookDto.getThumb())
                .userId(bookDto.getUserId())
                .likeCnt(bookDto.getLikeCnt())
                .category(category)
                .bookSubCategories(bookSubCategories)
                .blockYn(bookDto.isBlockYn())
                .blockDt(bookDto.getBlockDt())
                .saveDt(bookDto.getSaveDt())
                .updtDt(bookDto.getUpdtDt())
                .build();
    }
}
