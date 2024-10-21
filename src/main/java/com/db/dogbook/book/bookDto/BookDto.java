package com.db.dogbook.book.bookDto;

import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "책 이름은 비워둘 수 없습니다")
    @Size(max = 100, message = "책 이름은 100자 이내여야 합니다")
    private String bookName;

    @NotNull(message = "가격은 비워둘 수 없습니다")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다")
    private int price;

    @NotBlank(message = "저자 이름은 비워둘 수 없습니다")
    @Size(max = 50, message = "저자 이름은 50자 이내여야 합니다")
    private String author;

    private int fileIdx;

    @Size(max = 255, message = "썸네일 경로는 255자 이내여야 합니다")
    private String thumb;

    private int userId;

    @NotNull(message = "좋아요 수는 null일 수 없습니다")
    @Min(value = 0, message = "좋아요 수는 0 이상이어야 합니다")
    private Integer likeCnt = 0;  // 기본값을 0으로 설정

    @NotNull(message = "카테고리는 반드시 선택해야 합니다")
    private CategoryDto categoryDto;

    @NotNull(message = "서브카테고리는 반드시 선택해야 합니다")
    private SubCategoryDto subCategoryDto;

    private boolean blockYn;

    private LocalDateTime blockDt;

    private LocalDateTime saveDt;

    private LocalDateTime updtDt;
}
