package com.db.dogbook.category.service;

import com.db.dogbook.book.bookDto.BookDto;
import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.categoryDto.SubCategoryDto;
import com.db.dogbook.category.converter.CategoryConverter;
import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryConverter categoryConverter;

    private CategoryDto categoryDto;
    private SubCategoryDto subCategoryDto;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
        categoryDto = new CategoryDto();
        categoryDto.setCategoryName("Test Category");
        Category category = Category.builder()
                .categoryName(categoryDto.getCategoryName())
                .subCategories(new ArrayList<>())
                .books(new ArrayList<>())
                .build();

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
    void getAllCategories() {
        createCategory();
        List<CategoryDto> allCategories = categoryService.getAllCategories();

        assertNotNull(allCategories);
        assertEquals(1, allCategories.size());
    }

    @Test
    void findCategoryByName() {
        createCategory();
        CategoryDto categoryByCategoryName = categoryService.findCategoryByCategoryName(categoryDto.getCategoryName());
        assertNotNull(categoryByCategoryName);
        assertEquals("Test Category", categoryByCategoryName.getCategoryName());
    }

}