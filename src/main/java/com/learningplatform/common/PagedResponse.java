package com.learningplatform.common;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public record PagedResponse<T>(
        List<T> items,
        int pageNumber,
        int pageSize,
        long totalElements
) {
    public PagedResponse {
        if (items == null) {
            throw new IllegalArgumentException("Items is mandatory");
        }
        if (pageNumber < 0) {
            throw new IllegalArgumentException("pageNumber must be non-negative");
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must be positive");
        }
        if (totalElements < 0) {
            throw new IllegalArgumentException("totalElements must be non-negative");
        }

        if (items.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("items cannot contain null values");
        }
        if (items.size() > pageSize) {
            throw new IllegalArgumentException("items cannot contain more than " + pageSize + " items");
        }
        items = List.copyOf(items);

    }

    public long totalPages() {
        return totalElements / pageSize
                + (totalElements % pageSize == 0 ? 0 : 1);
    }

    public <R> PagedResponse<R> map(
            Function<? super T, ? extends R> mapper) {

        if (mapper == null) {
            throw new IllegalArgumentException("mapper is mandatory");
        }

        List<R> mappedItems = items.stream()
                .<R>map(mapper)
                .toList();

        return new PagedResponse<>(
                mappedItems, pageNumber, pageSize, totalElements);
    }

}