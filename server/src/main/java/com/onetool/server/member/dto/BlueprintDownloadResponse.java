package com.onetool.server.member.dto;

public record BlueprintDownloadResponse(
        Long blueprintId,
        String blueprintImage,
        String blueprintDownloadLink,
        String blueprintName,
        String blueprintCreatorName
) {}