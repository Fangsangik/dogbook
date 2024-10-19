package com.db.dogbook.category.controller;

import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/getAllCategories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> allCategories = categoryService.getAllCategories();
        return ResponseEntity.ok(allCategories);
    }

    @GetMapping("/getCategoryName")
    public ResponseEntity<CategoryDto> getCategoryName(@RequestParam String categoryName) {
        CategoryDto categoryByCategoryName = categoryService.findCategoryByCategoryName(categoryName);
        return ResponseEntity.ok(categoryByCategoryName);
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto createCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok().body(createCategory);
    }
}
