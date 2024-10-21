package com.db.dogbook.category.service;

import com.db.dogbook.category.categoryDto.CategoryDto;
import com.db.dogbook.category.converter.CategoryConverter;
import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;


    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryConverter::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto findCategoryByCategoryName(String name) {
        Category findCategoryName = categoryRepository.findByCategoryName(name)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));

        return categoryConverter.toCategoryDto(findCategoryName);
    }

    @Transactional
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        // id가 null이 아닌 경우 이미 존재하는 카테고리로 간주
        if (categoryDto.getId() != null) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }

        if (categoryDto.getCategoryName() == null) {
            throw new IllegalArgumentException("카테고리 이름이 null 이면 안됩니다.");
        }

        // 카테고리 이름이 중복되는지 확인
        if (categoryRepository.findByCategoryName(categoryDto.getCategoryName()).isPresent()) {
            throw new IllegalArgumentException("동일 이름이 있는 카테고리 입니다.");
        }

        Category category = Category.builder()
                .categoryName(categoryDto.getCategoryName())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return categoryConverter.toCategoryDto(savedCategory);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category updatedCategory = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Id가 존재하지 않습니다."));

        if (categoryDto.getId() == null) {
            throw new IllegalArgumentException("Id 값이 null 이면 안됩니다.");
        }

        if (categoryDto.getCategoryName() == null) {
            throw new IllegalArgumentException("카테고리 이름이 null 이면 안됩니다.");
        }

        updatedCategory.setCategoryName(categoryDto.getCategoryName());
        Category savedCategory = categoryRepository.save(updatedCategory);
        return categoryConverter.toCategoryDto(savedCategory);
    }


    @Transactional
    @Override
    public void deleteCategory(CategoryDto categoryDto) {
        Category deleteCategory = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Id가 존재하지 않습니다."));

        categoryRepository.deleteById(categoryDto.getId());
    }
}
