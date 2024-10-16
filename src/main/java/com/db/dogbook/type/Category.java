package com.db.dogbook.type;

import lombok.Getter;

import static com.db.dogbook.type.Category.SubCategory.*;

@Getter
public enum Category {
    BACKEND(new SubCategory[]{JAVA, SPRING}),
    FRONTEND(new SubCategory[]{JavaScript}),
    DESIGN(new SubCategory[]{CSS, UI, UX})
    ;

    private final SubCategory[] subCategories;

    Category(SubCategory[] subCategories) {
        this.subCategories = subCategories;
    }

    public enum SubCategory {
        JAVA("Java"),
        SPRING("Spring"),
        HTML("HTML"),
        CSS("CSS"),
        UI("UI Design"),
        UX("UX Design"),
        JavaScript("JavaScript"),;

        private final String subCategoryName;

        SubCategory(String subCategoryName) {
            this.subCategoryName = subCategoryName;
        }
    }
}
