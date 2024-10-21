package com.db.dogbook.category.service;


import com.db.dogbook.category.categoryDto.CategoryDto;

import com.db.dogbook.category.converter.CategoryConverter;
import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryConverter categoryConverter;

    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        // 데이터베이스를 초기화하여 테스트 간의 충돌 방지
        categoryRepository.deleteAll();
        categoryDto = new CategoryDto();
        categoryDto.setCategoryName("Test Category");

        // 카테고리 생성
        Category category = Category.builder()
                .categoryName(categoryDto.getCategoryName())
                .build();

        // 저장 후 DTO로 변환
        categoryDto = categoryConverter.toCategoryDto(categoryRepository.save(category));
    }

    @Test
    @Transactional
    void createCategory() {
        // categoryName이 올바르게 설정되었는지 확인
        assertNotNull(categoryDto);
        assertEquals("Test Category", categoryDto.getCategoryName());

        // 이미 존재하는 카테고리를 생성하려고 하면 예외 발생
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.createCategory(categoryDto);
        }, "이미 존재하는 카테고리입니다.");
    }

    @Test
    @Transactional
    void fail_createCategory_dueToDuplicate() {
        // 동일한 이름의 카테고리 생성 시도
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.createCategory(categoryDto);  // 중복된 categoryDto로 시도
        });

        // 예외 메시지 검증
        assertEquals("이미 존재하는 카테고리입니다.", exception.getMessage());
    }

    @Test
    @Transactional
    void testCreateAndFindCategory() {
        createCategory();

        // 생성된 카테고리 이름으로 검색
        CategoryDto foundCategory = categoryService.findCategoryByCategoryName(categoryDto.getCategoryName());

        assertNotNull(foundCategory);
        assertEquals("Test Category", foundCategory.getCategoryName());
    }

    @Test
    void getAllCategories() {
        createCategory();
        List<CategoryDto> allCategories = categoryService.getAllCategories();

        assertNotNull(allCategories);
        assertEquals(1, allCategories.size());
    }

    @AfterEach
    void tearDown() {
        // 각 테스트 후 데이터 정리
        categoryRepository.deleteAll();
    }
}
