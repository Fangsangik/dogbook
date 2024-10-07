package com.db.dogbook.book.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    //??
    private int fileIdx;

    private String thumb;
    private int userId;
    private int likeCnt;

    //상속으로 풀 예정
    private String category;

    private boolean blockYn;

    private LocalDateTime blockDt;
    private LocalDateTime saveDt;
    private LocalDateTime updtDt;
}
