package com.db.dogbook.category.categoryDto;

import com.db.dogbook.book.bookDto.BookDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookSubCategoryDto {
    private Long id;
    private BookDto bookDto; // 책 ID로만 처리
    private SubCategoryDto subCategoryDto; // 서브 카테고리 ID로만 처리
}