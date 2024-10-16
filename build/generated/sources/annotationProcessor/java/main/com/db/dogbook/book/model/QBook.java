package com.db.dogbook.book.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = -2066246617L;

    public static final QBook book = new QBook("book");

    public final StringPath author = createString("author");

    public final DateTimePath<java.time.LocalDateTime> blockDt = createDateTime("blockDt", java.time.LocalDateTime.class);

    public final BooleanPath blockYn = createBoolean("blockYn");

    public final StringPath bookName = createString("bookName");

    public final StringPath category = createString("category");

    public final NumberPath<Integer> fileIdx = createNumber("fileIdx", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> likeCnt = createNumber("likeCnt", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> saveDt = createDateTime("saveDt", java.time.LocalDateTime.class);

    public final StringPath thumb = createString("thumb");

    public final DateTimePath<java.time.LocalDateTime> updtDt = createDateTime("updtDt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QBook(String variable) {
        super(Book.class, forVariable(variable));
    }

    public QBook(Path<? extends Book> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBook(PathMetadata metadata) {
        super(Book.class, metadata);
    }

}

