package com.db.dogbook.category.service;

import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.converter.CategoryConverter;
import com.db.dogbook.category.converter.SubCategoryConverter;
import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.domain.SubCategory;
import com.db.dogbook.category.repository.CategoryRepository;
import com.db.dogbook.category.repository.SubCategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SubCategoryServiceTest {

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryConverter categoryConverter;

    @Autowired
    private SubCategoryConverter subCategoryConverter;

    private CategoryDto categoryDto;
    private SubCategoryDto subCategoryDto;

    @BeforeEach
    void setUp() {
        // 데이터 초기화
        subCategoryRepository.deleteAll();
        categoryRepository.deleteAll();

        // CategoryDto와 SubCategoryDto 객체를 명시적으로 초기화
        categoryDto = new CategoryDto();  // categoryDto 초기화
        subCategoryDto = new SubCategoryDto();  // subCategoryDto 초기화

        // categoryDto에 이름 설정
        categoryDto.setCategoryName("Test Category");

        // 카테고리 저장
        Category category = categoryRepository.save(
                Category.builder()
                        .categoryName(categoryDto.getCategoryName())
                        .build()
        );

        // subCategoryDto에 이름 설정
        subCategoryDto.setSubCategoryName("Test Sub Category");

        // 서브 카테고리 저장
        SubCategory subCategory = subCategoryRepository.save(
                SubCategory.builder()
                        .category(category)
                        .subCategoryName(subCategoryDto.getSubCategoryName())
                        .build()
        );

        // DTO로 변환
        categoryDto = categoryConverter.toCategoryDto(category);
        subCategoryDto = subCategoryConverter.toSubCategoryDto(subCategory);
    }

    @Test
    void getSubCategoriesByCategoryId() {
        List<SubCategoryDto> subCategoriesByCategoryId = subCategoryService.getSubCategoriesByCategoryId(categoryDto.getId());
        assertNotNull(subCategoriesByCategoryId);
        assertThat(subCategoriesByCategoryId.size()).isEqualTo(1);
        assertThat(subCategoriesByCategoryId.get(0).getSubCategoryName()).isEqualTo("Test Sub Category");
    }

    @Test
    @Transactional
    void createSubCategory() {
        SubCategoryDto subCategory = subCategoryService.createSubCategory(subCategoryDto);

        assertNotNull(subCategory);
        assertEquals("Test Sub Category", subCategory.getSubCategoryName());

    }

    @Test
    void assignToCategory() {
        // SubCategoryDto 생성 및 ID 설정
        SubCategoryDto subCategoryDto = new SubCategoryDto();
        subCategoryDto.setId(1L); // 존재하는 서브 카테고리 ID로 설정

        // CategoryDto 생성 및 ID 설정
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L); // 존재하는 카테고리 ID로 설정
        subCategoryDto.setCategoryDto(categoryDto);

        // Category에 SubCategory를 할당
        SubCategoryDto assigned = subCategoryService.assignToCategory(subCategoryDto, 1L);

        // 검증 로직
        assertNotNull(assigned);
        assertEquals(1L, assigned.getCategoryDto().getId());
    }
}