package com.onetool.server.api.blueprint.dto;

import com.onetool.server.api.blueprint.Blueprint;
import jakarta.persistence.Column;
import lombok.Builder;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Builder
public record SearchResponse(
        Long id,
        String blueprintName,
        Long categoryId,
        Long standardPrice,
        String blueprintImg,
        String blueprintDetails,
        String extension,
        String program,
        BigInteger hits,
        Long salePrice,
        LocalDateTime saleExpiredDate,
        String creatorName,
        String downloadLink,
        String secondCategory
) {

    public static SearchResponse from(Blueprint blueprint) {
        return SearchResponse.builder()
                .id(blueprint.getId())
                .blueprintName(blueprint.getBlueprintName())
                .categoryId(blueprint.getCategoryId())
                .standardPrice(blueprint.getStandardPrice())
                .blueprintImg(blueprint.getBlueprintImg())
                .blueprintDetails(blueprint.getBlueprintDetails())
                .extension(blueprint.getExtension())
                .program(blueprint.getProgram())
                .hits(blueprint.getHits())
                .salePrice(blueprint.getSalePrice())
                .saleExpiredDate(blueprint.getSaleExpiredDate())
                .creatorName(blueprint.getCreatorName())
                .downloadLink(blueprint.getDownloadLink())
                .secondCategory(blueprint.getSecondCategory())
                .build();
    }
}
