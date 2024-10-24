package com.db.dogbook.book.model;


import com.db.dogbook.category.domain.Category;
import com.db.dogbook.category.domain.SubCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) // 카테고리와의 관계에 Cascade 설정
    @JoinColumn(name = "category_id", nullable = true)  // nullable 설정
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private SubCategory subCategory;

    //차단
    private boolean blockYn;

    private LocalDateTime blockDt;
    private LocalDateTime saveDt;
    private LocalDateTime updtDt;

    // 엔티티 저장 전 기본값을 설정하는 메서드
    @PrePersist
    public void prePersist() {
        if (this.likeCnt == null) {
            this.likeCnt = 0;  // null일 경우 기본값 설정
        }
    }
}
