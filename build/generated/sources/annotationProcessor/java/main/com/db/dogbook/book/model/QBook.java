package com.db.dogbook.book.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = -2066246617L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBook book = new QBook("book");

    public final StringPath author = createString("author");

    public final DateTimePath<java.time.LocalDateTime> blockDt = createDateTime("blockDt", java.time.LocalDateTime.class);

    public final BooleanPath blockYn = createBoolean("blockYn");

    public final StringPath bookName = createString("bookName");

    public final com.db.dogbook.category.domain.QCategory category;

    public final NumberPath<Integer> fileIdx = createNumber("fileIdx", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> likeCnt = createNumber("likeCnt", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> saveDt = createDateTime("saveDt", java.time.LocalDateTime.class);

    public final com.db.dogbook.category.domain.QSubCategory subCategory;

    public final StringPath thumb = createString("thumb");

    public final DateTimePath<java.time.LocalDateTime> updtDt = createDateTime("updtDt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QBook(String variable) {
        this(Book.class, forVariable(variable), INITS);
    }

    public QBook(Path<? extends Book> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBook(PathMetadata metadata, PathInits inits) {
        this(Book.class, metadata, inits);
    }

    public QBook(Class<? extends Book> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.db.dogbook.category.domain.QCategory(forProperty("category")) : null;
        this.subCategory = inits.isInitialized("subCategory") ? new com.db.dogbook.category.domain.QSubCategory(forProperty("subCategory"), inits.get("subCategory")) : null;
    }

}

