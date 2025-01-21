package com.onetool.server.api.blueprint.dto;

import com.onetool.server.api.blueprint.Blueprint;
import lombok.Builder;

public record SearchResponse(
        Blueprint blueprint
) {

    @Builder
    public SearchResponse(Blueprint blueprint) {
        this.blueprint = blueprint;
    }

    public static SearchResponse from(Blueprint blueprint) {
        return SearchResponse.builder()
                .blueprint(blueprint)
                .build();
    }
}
