package com.onetool.server.api.blueprint.enums;

import com.onetool.server.global.exception.InvalidSortTypeException;
import org.springframework.data.domain.Sort;

public enum SortType {
        PRICE,
        CREATED_AT,
        EXTENSION;

        public static Sort getSortBySortType(SortType sortType, String sortOrder) {
                Sort.Direction direction = getDirection(sortOrder);

                if (sortType == SortType.PRICE) {
                        return getPriceSort(direction);
                }

                if (sortType == SortType.CREATED_AT) {
                        return Sort.by(Sort.Order.by("createdAt").with(direction).nullsLast());
                }

                if (sortType == SortType.EXTENSION) {
                        return Sort.by(Sort.Order.by("extension").with(direction).nullsLast());
                }

                throw new InvalidSortTypeException();
        }

        private static Sort.Direction getDirection(String sortOrder) {
                if ("desc".equalsIgnoreCase(sortOrder)) {
                        return Sort.Direction.DESC;
                }
                return Sort.Direction.ASC;
        }

        private static Sort getPriceSort(Sort.Direction direction) {
                return Sort.by(
                        Sort.Order.by("saleExpiredDate").with(Sort.Direction.DESC).nullsLast(),
                        Sort.Order.by("salePrice").with(direction).nullsLast(),
                        Sort.Order.by("standardPrice").with(direction).nullsLast()
                );
        }
}