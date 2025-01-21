package com.onetool.server.blueprint.dto;

import com.onetool.server.blueprint.Blueprint;
import lombok.Builder;

import java.util.List;

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
