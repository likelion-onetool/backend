package com.onetool.server.api.blueprint.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BlueprintUpdateRequest(
        Long id,
        String blueprintName,
        Long categoryId,
        Long standardPrice,
        String blueprintImg,
        String blueprintDetails,
        String extension,
        String program,
        Long salePrice,
        LocalDateTime saleExpiredDate,
        String creatorName,
        String downloadLink,
        String detailImage,
        Long hits,
        Boolean isDeleted
) {

}
