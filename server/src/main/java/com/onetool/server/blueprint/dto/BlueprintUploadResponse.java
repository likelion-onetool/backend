package com.onetool.server.blueprint.dto;

import lombok.Builder;

@Builder
public record BlueprintUploadResponse(
    String fileUrl
) {
    public static BlueprintUploadResponse of(String fileUrl) {
        return BlueprintUploadResponse.builder()
                .fileUrl(fileUrl)
                .build();
    }
}
