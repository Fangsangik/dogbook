package com.db.dogbook.category.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookSubCategory is a Querydsl query type for BookSubCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookSubCategory extends EntityPathBase<BookSubCategory> {

    private static final long serialVersionUID = -1344632913L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookSubCategory bookSubCategory = new QBookSubCategory("bookSubCategory");

    public final com.db.dogbook.book.model.QBook book;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QSubCategory subCategory;

    public QBookSubCategory(String variable) {
        this(BookSubCategory.class, forVariable(variable), INITS);
    }

    public QBookSubCategory(Path<? extends BookSubCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookSubCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookSubCategory(PathMetadata metadata, PathInits inits) {
        this(BookSubCategory.class, metadata, inits);
    }

    public QBookSubCategory(Class<? extends BookSubCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new com.db.dogbook.book.model.QBook(forProperty("book"), inits.get("book")) : null;
        this.subCategory = inits.isInitialized("subCategory") ? new QSubCategory(forProperty("subCategory"), inits.get("subCategory")) : null;
    }

}

