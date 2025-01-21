package com.onetool.server.api.blueprint.controller;

import com.onetool.server.api.blueprint.dto.BlueprintResponse;
import com.onetool.server.api.blueprint.dto.BlueprintSortRequest;
import com.onetool.server.api.blueprint.dto.SearchResponse;
import com.onetool.server.api.blueprint.service.BlueprintSearchService;
import com.onetool.server.api.category.FirstCategoryType;
import com.onetool.server.global.exception.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
public class SearchController {

    private final BlueprintSearchService blueprintSearchService;

    public SearchController(BlueprintSearchService blueprintSearchService) {
        this.blueprintSearchService = blueprintSearchService;
    }

    @GetMapping("/blueprint")
    public ApiResponse<Page<SearchResponse>> searchWithKeyword(
            @RequestParam("s")String keyword,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable
    ) {
        String decodedKeyword = URLDecoder.decode(keyword, StandardCharsets.UTF_8);
        log.info("Decoded keyword: {}", decodedKeyword);
        Page<SearchResponse> response = blueprintSearchService.searchNameAndCreatorWithKeyword(decodedKeyword, pageable);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/blueprint/building")
    public ApiResponse<Page<SearchResponse>> searchBuildingCategory(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintSearchService.findAllByCategory(FirstCategoryType.CATEGORY_BUILDING, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @GetMapping("/blueprint/civil")
    public ApiResponse<Page<SearchResponse>> searchCivilCategory(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintSearchService.findAllByCategory(FirstCategoryType.CATEGORY_CIVIL, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @GetMapping("/blueprint/interior")
    public ApiResponse<Page<SearchResponse>> searchInteriorCategory(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintSearchService.findAllByCategory(FirstCategoryType.CATEGORY_INTERIOR, category, pageable);
        return ApiResponse.onSuccess(responses);
    }
    @GetMapping("/blueprint/machine")
    public ApiResponse<Page<SearchResponse>> searchMachineCategory(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintSearchService.findAllByCategory(FirstCategoryType.CATEGORY_MACHINE, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @GetMapping("/blueprint/electric")
    public ApiResponse<Page<SearchResponse>> searchElectricCategory(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintSearchService.findAllByCategory(FirstCategoryType.CATEGORY_ELECTRIC, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @GetMapping("/blueprint/etc")
    public ApiResponse<Page<SearchResponse>> searchEtcCategory(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable
    ) {
        Page<SearchResponse> responses = blueprintSearchService.findAllByCategory(FirstCategoryType.CATEGORY_CIVIL, null, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @GetMapping("/blueprint/all")
    public ApiResponse<Page<SearchResponse>> searchAllBlueprint(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable
    ) {
        Page<SearchResponse> responses = blueprintSearchService.findAll(pageable);
        return ApiResponse.onSuccess(responses);
    }

    @GetMapping("/blueprint/{id}")
    public ApiResponse<BlueprintResponse> getBlueprintDetails(@PathVariable("id") Long id) {
        BlueprintResponse blueprintResponseDTO = blueprintSearchService.findApprovedBlueprintById(id);
        return ApiResponse.onSuccess(blueprintResponseDTO);
    }

    @GetMapping({"/blueprint/sort", "/blueprint/{categoryName}/sort"})
    public ApiResponse<List<BlueprintResponse>> sortBlueprints(
            @PathVariable(name = "categoryName", required = false) String categoryName,
            @RequestParam(name = "sortBy") String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "asc") String sortOrder,
            Pageable pageable
    ) {
        BlueprintSortRequest request = new BlueprintSortRequest(categoryName, sortBy, sortOrder);
        List<BlueprintResponse> sortedItems = blueprintSearchService.sortBlueprints(request, pageable);
        return ApiResponse.onSuccess(sortedItems);
    }
}