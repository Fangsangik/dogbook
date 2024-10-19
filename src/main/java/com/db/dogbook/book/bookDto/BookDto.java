package com.db.dogbook.book.bookDto;

import com.db.dogbook.category.categoryDto.BookSubCategoryDto;
import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.domain.BookSubCategory;
import com.db.dogbook.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String bookName;
    private int price;
    private String author;

    private int fileIdx;
    private String thumb;
    private int userId;
    private int likeCnt;
    private CategoryDto categoryDto;

    private List<BookSubCategoryDto> bookSubCategoryDtos = new ArrayList<>();
    private boolean blockYn;
    private LocalDateTime blockDt;
    private LocalDateTime saveDt;
    private LocalDateTime updtDt;
}
