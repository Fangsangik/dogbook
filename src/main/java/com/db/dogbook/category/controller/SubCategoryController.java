package com.db.dogbook.category.controller;

import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subCategory")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;


    @PostMapping("/create")
    public ResponseEntity<SubCategoryDto> createSubCategory(@RequestBody SubCategoryDto subCategoryDto) {

        SubCategoryDto createSubCategory = subCategoryService.createSubCategory(subCategoryDto);
        return ResponseEntity.ok().body(createSubCategory);
    }

    @PostMapping("/{subCategoryId}/assginToCategory/{categoryId}")
    public ResponseEntity<SubCategoryDto> assignSubCategoryToCategory
            (@RequestBody SubCategoryDto subCategoryDto,
             @PathVariable Long categoryId) {
        SubCategoryDto assignToCategory = subCategoryService.assignToCategory(subCategoryDto, categoryId);
        return ResponseEntity.ok().body(assignToCategory);
    }
}
