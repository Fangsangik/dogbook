package com.db.dogbook.category.controller;

import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.service.SubCategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subCategory")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryServiceImpl subCategoryServiceImpl;

    @PostMapping("/create")
    public ResponseEntity<SubCategoryDto> createSubCategory(@RequestBody SubCategoryDto subCategoryDto) {
        SubCategoryDto createSubCategory = subCategoryServiceImpl.createSubCategory(subCategoryDto);
        return ResponseEntity.ok().body(createSubCategory);
    }

    @PostMapping("/{subCategoryId}/assignToCategory/{categoryId}") // 오타 수정
    public ResponseEntity<SubCategoryDto> assignSubCategoryToCategory(
            @PathVariable Long subCategoryId,
            @PathVariable Long categoryId) {
        SubCategoryDto assignToCategory = subCategoryServiceImpl.assignToCategory(subCategoryId, categoryId);
        return ResponseEntity.ok().body(assignToCategory);
    }
}