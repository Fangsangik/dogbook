package com.db.dogbook.category.service;

import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.converter.CategoryConverter;
import com.db.dogbook.category.converter.SubCategoryConverter;
import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.domain.SubCategory;
import com.db.dogbook.category.repository.CategoryRepository;
import com.db.dogbook.category.repository.SubCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SubCategoryServiceTest {

    @Autowired
    private SubCategoryServiceImpl subCategoryServiceImpl;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

        // CategoryDto와 SubCategoryDto를 명시적으로 생성 및 초기화
        categoryDto = CategoryDto.builder()
                .categoryName("Test Category")
                .build();

        subCategoryDto = SubCategoryDto.builder()
                .subCategoryName("Test Sub Category")
                .build();

        // 카테고리 생성 및 저장
        Category category = Category.builder()
                .categoryName(categoryDto.getCategoryName())
                .subCategories(List.of())
                .books(List.of())
                .build();
        category = categoryRepository.save(category);

        // 서브 카테고리 생성 및 저장
        SubCategory subCategory = SubCategory.builder()
                .category(category)
                .subCategoryName(subCategoryDto.getSubCategoryName())
                .books(new ArrayList<>())
                .build();
        subCategory = subCategoryRepository.save(subCategory);

        subCategory.setCategory(category);

        // DTO로 변환
        categoryDto = categoryConverter.toCategoryDto(category);
        subCategoryDto = subCategoryConverter.toSubCategoryDto(subCategory);
    }

    @Test
    void getSubCategoriesByCategoryId() {
        List<SubCategoryDto> subCategoriesByCategoryId = subCategoryServiceImpl.getSubCategoriesByCategoryId(categoryDto.getId());
        assertNotNull(subCategoriesByCategoryId);
        assertThat(subCategoriesByCategoryId.size()).isEqualTo(1);
        assertThat(subCategoriesByCategoryId.get(0).getSubCategoryName()).isEqualTo("Test Sub Category");
    }

    @Test
    @Transactional
    void createSubCategory() {
        SubCategoryDto newSubCategory = subCategoryServiceImpl.createSubCategory(subCategoryDto);

        assertNotNull(newSubCategory);
        assertEquals("Test Sub Category", newSubCategory.getSubCategoryName());
    }

    @Test
    @Transactional
    void assignToCategory() {
        // 새로운 서브 카테고리 생성 및 저장
        createSubCategory();
        // 저장된 서브카테고리의 ID를 사용하여 카테고리에 할당
        SubCategoryDto assignedSubCategory = subCategoryServiceImpl.assignToCategory(subCategoryDto.getId(), categoryDto.getId());

        // 검증 로직
        assertNotNull(assignedSubCategory);
        assertEquals(categoryDto.getId(), assignedSubCategory.getCategoryDto().getId());
    }

}

