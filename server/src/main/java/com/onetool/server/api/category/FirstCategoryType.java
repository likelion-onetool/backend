package com.onetool.server.api.category;

import com.onetool.server.global.exception.CategoryNotFoundException;
import com.onetool.server.global.exception.codes.ErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum FirstCategoryType {
    CATEGORY_BUILDING(1L, "building"),
    CATEGORY_CIVIL(2L, "civil"),
    CATEGORY_INTERIOR(3L, "interior"),
    CATEGORY_MACHINE(4L, "machine"),
    CATEGORY_ELECTRIC(5L, "electric"),
    CATEGORY_ETC(6L, "etc");

    private final Long categoryId;
    private final String type;

    FirstCategoryType(Long categoryId, String type) {
        this.categoryId = categoryId;
        this.type = type;
    }

    public static FirstCategoryType findByCategoryId(Long categoryId) {
        return Arrays.stream(values())
                .filter(category -> category.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new CategoryNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public static FirstCategoryType findByType(String type) {
        return Arrays.stream(values())
                .filter(category -> category.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new CategoryNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}