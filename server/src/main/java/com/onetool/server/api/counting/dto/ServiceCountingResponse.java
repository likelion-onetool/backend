package com.onetool.server.api.counting.dto;

import lombok.Builder;

public record ServiceCountingResponse(
        Long totalFiles,
        Long totalUser,
        Long totalSales
) {

    @Builder
    public ServiceCountingResponse(Long totalFiles, Long totalUser, Long totalSales) {
        this.totalFiles = totalFiles;
        this.totalUser = totalUser;
        this.totalSales = totalSales;
    }
}