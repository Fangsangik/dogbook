package com.db.dogbook.category.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subCategoryName;

    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "subCategory", fetch = LAZY, cascade = ALL, orphanRemoval = true)
    private List<BookSubCategory> bookSubCategories = new ArrayList<>();

    /*
    다대다 관계
    하나의 책이 여러 서브 카테고리에 속할 수도 있고, 동시에 하나의 서브 카테고리도 여러 책을 갖을 수 있는 경우

    다대일 관계
    여러 책이 하나의 서브 카테고리에만 속할 수 있는 경우
    하나의 서브 카테고리만 가져올 수 있을때
     */
}
