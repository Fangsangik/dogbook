package com.db.dogbook.book.model;


import com.db.dogbook.type.Category;
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
    private int likeCnt;

    //Enumtype
    @Enumerated(EnumType.STRING)
    private Category category;

    //차단
    private boolean blockYn;

    private LocalDateTime blockDt;
    private LocalDateTime saveDt;
    private LocalDateTime updtDt;
}
