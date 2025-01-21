package com.onetool.server.api.blueprint.dto;

public record BlueprintSortRequest(
        String categoryName,
        String sortBy,
        String sortOrder
) {}