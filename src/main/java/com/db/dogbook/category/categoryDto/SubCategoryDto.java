package com.db.dogbook.category.categoryDto;

import com.db.dogbook.book.bookDto.BookDto;
import com.db.dogbook.book.model.Book;
import com.db.dogbook.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDto {

    private Long id;
    private String subCategoryName;
    private CategoryDto categoryDto;
    private List<BookDto> bookDtos = new ArrayList<>();


 }
