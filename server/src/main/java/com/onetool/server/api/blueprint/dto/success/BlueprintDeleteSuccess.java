package com.onetool.server.api.blueprint.dto.success;

import lombok.Builder;

@Builder
public record BlueprintDeleteSuccess(
        boolean isSuccess
) {

    public static BlueprintDeleteSuccess of(boolean success) {
        return BlueprintDeleteSuccess.builder().isSuccess(success).build();
    }
}
