package com.onetool.server.api.blueprint.dto.response;

import com.onetool.server.api.blueprint.Blueprint;
import lombok.Builder;
import org.springframework.data.domain.Page;


import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record BlueprintResponse(
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
        boolean isDeleted,
        String detailImage
) {

    @Builder
    public static BlueprintResponse from(Blueprint blueprint) {
        return new BlueprintResponse(
                blueprint.getId(),
                blueprint.getBlueprintName(),
                blueprint.getCategoryId(),
                blueprint.getStandardPrice(),
                blueprint.getBlueprintImg(),
                blueprint.getBlueprintDetails(),
                blueprint.getExtension(),
                blueprint.getProgram(),
                blueprint.getHits(),
                blueprint.getSalePrice(),
                blueprint.getSaleExpiredDate(),
                blueprint.getCreatorName(),
                blueprint.getDownloadLink(),
                blueprint.getIsDeleted(),
                blueprint.getDetailImage()
        );
    }

    public static List<BlueprintResponse> toBlueprintResponseList(List<Blueprint> blueprintList) {
        return blueprintList.stream()
                .map(BlueprintResponse::from)
                .toList();
    }

    public static List<BlueprintResponse> fromBlueprintPageToResponseList(Page<Blueprint> blueprintPage) {
        return blueprintPage.stream()
                .map(BlueprintResponse::from)
                .collect(Collectors.toList());
    }

    public static BlueprintResponse items(Blueprint blueprint) {
        return BlueprintResponse.builder()
                .id(blueprint.getId())
                .creatorName(blueprint.getCreatorName())
                .blueprintName(blueprint.getBlueprintName())
                .standardPrice(blueprint.getStandardPrice())
                .salePrice(blueprint.getSalePrice())
                .blueprintImg(blueprint.getBlueprintImg())
                .isDeleted(blueprint.getIsDeleted())
                .build();
    }
}