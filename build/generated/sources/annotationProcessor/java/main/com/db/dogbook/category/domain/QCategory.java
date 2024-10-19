package com.db.dogbook.category.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = -922350780L;

    public static final QCategory category = new QCategory("category");

    public final ListPath<com.db.dogbook.book.model.Book, com.db.dogbook.book.model.QBook> books = this.<com.db.dogbook.book.model.Book, com.db.dogbook.book.model.QBook>createList("books", com.db.dogbook.book.model.Book.class, com.db.dogbook.book.model.QBook.class, PathInits.DIRECT2);

    public final StringPath categoryName = createString("categoryName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<SubCategory, QSubCategory> subCategories = this.<SubCategory, QSubCategory>createList("subCategories", SubCategory.class, QSubCategory.class, PathInits.DIRECT2);

    public QCategory(String variable) {
        super(Category.class, forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }

}

