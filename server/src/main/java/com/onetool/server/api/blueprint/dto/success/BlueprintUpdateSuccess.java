package com.onetool.server.api.blueprint.dto.success;

import lombok.Builder;

@Builder
public record BlueprintUpdateSuccess(
        boolean isSuccess,
        Long blueprintId
) {

    public static BlueprintUpdateSuccess of(boolean success, Long blueprintId) {
        return BlueprintUpdateSuccess.builder().isSuccess(success).blueprintId(blueprintId).build();
    }
}
