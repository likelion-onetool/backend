package com.onetool.server.api.category;

import lombok.Getter;

@Getter
public enum FirstCategoryType {
    CATEGORY_BUILDING("building"),
    CATEGORY_CIVIL("civil"),
    CATEGORY_INTERIOR("interior"),
    CATEGORY_MACHINE("machine"),
    CATEGORY_ELECTRIC("electric"),
    CATEGORY_ETC("etc");

    private final String type;

    FirstCategoryType(String type) {
        this.type = type;
    }

}
