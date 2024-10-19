package com.db.dogbook.category.repository;
import com.db.dogbook.category.domain.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findByCategoryId(Long categoryId); //// 카테고리 ID로 서브 카테고리 조회
}
