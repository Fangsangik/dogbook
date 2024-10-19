package com.db.dogbook.book.model;


import com.db.dogbook.category.domain.BookSubCategory;
import com.db.dogbook.category.domain.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookName;
    private int price;
    private String author;

    //file table
    private int fileIdx;

    //썸네일
    private String thumb;
    private int userId;

    @Column(nullable = false)
    private Integer likeCnt = 0;  // 기본값을 0으로 설정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private List<BookSubCategory> bookSubCategories = new ArrayList<>();

    //차단
    private boolean blockYn;

    private LocalDateTime blockDt;
    private LocalDateTime saveDt;
    private LocalDateTime updtDt;

}
