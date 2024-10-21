package com.db.dogbook.book.bookDto;

import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private SubCategoryDto subCategoryDto;

    private boolean blockYn;
    private LocalDateTime blockDt;
    private LocalDateTime saveDt;
    private LocalDateTime updtDt;
}
