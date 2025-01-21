package com.onetool.server.blueprint.dto;

import lombok.Builder;

@Builder
public record BlueprintUploadRequest(
        String blueprintName,
        Long categoryId,
        Long standardPrice,
        String blueprintDetails,
        String extension,
        String program,
        String creatorName
) {}