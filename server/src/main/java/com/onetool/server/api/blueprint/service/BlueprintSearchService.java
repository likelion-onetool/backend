package com.onetool.server.api.blueprint.service;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.InspectionStatus;
import com.onetool.server.api.blueprint.dto.BlueprintResponse;
import com.onetool.server.api.blueprint.dto.BlueprintSortRequest;
import com.onetool.server.api.blueprint.dto.SearchResponse;
import com.onetool.server.api.blueprint.enums.SortType;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.api.category.FirstCategoryType;
import com.onetool.server.global.exception.BlueprintNotApprovedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import com.onetool.server.global.exception.BlueprintNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class BlueprintSearchService {
    private final BlueprintRepository blueprintRepository;

    public BlueprintSearchService(BlueprintRepository blueprintRepository) {
        this.blueprintRepository = blueprintRepository;
    }

    public BlueprintResponse findApprovedBlueprintById(Long id) {
        Blueprint blueprint = blueprintRepository.findById(id)
                .orElseThrow(BlueprintNotFoundException::new);

        if (blueprint.getInspectionStatus() != InspectionStatus.PASSED) {
            throw new BlueprintNotApprovedException();
        }

        return BlueprintResponse.from(blueprint);
    }

    public List<BlueprintResponse> sortBlueprints(BlueprintSortRequest request, Pageable pageable) {
        Long categoryId = getCategoryId(request.categoryName());
        Sort sort = SortType.getSortBySortType(SortType.valueOf(request.sortBy().toUpperCase()), request.sortOrder());
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        if (categoryId == null) {
            return sortBlueprintsWithoutCategory(sortedPageable);
        }

        return sortBlueprintsWithCategory(categoryId, sortedPageable);
    }

    private List<BlueprintResponse> sortBlueprintsWithoutCategory(Pageable sortedPageable) {
        Page<Blueprint> blueprintPage = blueprintRepository.findByInspectionStatus(InspectionStatus.PASSED, sortedPageable);
        return blueprintPage.stream()
                .map(BlueprintResponse::from)
                .collect(Collectors.toList());
    }

    private List<BlueprintResponse> sortBlueprintsWithCategory(Long categoryId, Pageable sortedPageable) {
        FirstCategoryType category = FirstCategoryType.findByCategoryId(categoryId);
        Page<Blueprint> blueprintPage = blueprintRepository.findAllByFirstCategory(
                category.getType(), InspectionStatus.PASSED, sortedPageable);
        return blueprintPage.stream()
                .map(BlueprintResponse::from)
                .collect(Collectors.toList());
    }

    public Page<SearchResponse> searchNameAndCreatorWithKeyword(String keyword, Pageable pageable) {
        Page<Blueprint> page = blueprintRepository.findAllNameAndCreatorContaining(keyword, InspectionStatus.PASSED, pageable);
        List<Blueprint> withOrderBlueprints = blueprintRepository.findWithOrderBlueprints(page.getContent());
        List<Blueprint> withCartBlueprints = blueprintRepository.findWithCartBlueprints(withOrderBlueprints);

        List<SearchResponse> list = convertToSearchResponseList(withCartBlueprints);
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    public Page<SearchResponse> findAllByCategory(FirstCategoryType firstCategory, String secondCategory, Pageable pageable) {
        if (secondCategory == null) {
            return findAllByFirstCategory(firstCategory, pageable);
        }
        return findAllBySecondCategory(firstCategory, secondCategory, pageable);
    }

    public Page<SearchResponse> findAll(Pageable pageable) {
        Page<Blueprint> result = blueprintRepository.findByInspectionStatus(InspectionStatus.PASSED, pageable);
        List<SearchResponse> list = convertToSearchResponseList(result.getContent());
        return new PageImpl<>(list, pageable, result.getTotalElements());
    }

    private Page<SearchResponse> findAllByFirstCategory(FirstCategoryType category, Pageable pageable) {
        Page<Blueprint> result = blueprintRepository.findAllByFirstCategory(category.getType(), InspectionStatus.PASSED, pageable);
        List<SearchResponse> list = result.getContent().stream()
                .map(SearchResponse::from)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, result.getTotalElements());
    }

    private Page<SearchResponse> findAllBySecondCategory(FirstCategoryType firstCategory, String secondCategory, Pageable pageable) {
        Page<Blueprint> result = blueprintRepository.findAllBySecondCategory(
                firstCategory.getType(), secondCategory, InspectionStatus.PASSED, pageable);
        List<SearchResponse> list = result.getContent().stream()
                .map(SearchResponse::from)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, result.getTotalElements());
    }

    private List<SearchResponse> convertToSearchResponseList(List<Blueprint> blueprints) {
        return blueprints.stream()
                .map(SearchResponse::from)
                .collect(Collectors.toList());
    }

    private Long getCategoryId(String categoryName) {
        if (categoryName == null) {
            return null;
        }
        return FirstCategoryType.findByType(categoryName).getCategoryId();
    }
}