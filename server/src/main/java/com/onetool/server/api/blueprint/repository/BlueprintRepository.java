package com.onetool.server.api.blueprint.repository;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.InspectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlueprintRepository extends JpaRepository<Blueprint, Long> {

    @Query(value = """
        SELECT b
        FROM Blueprint b
        WHERE (b.blueprintName LIKE %:keyword% OR b.creatorName LIKE %:keyword%)
          AND b.inspectionStatus = :status
    """)
    Page<Blueprint> findAllNameAndCreatorContaining(@Param("keyword") String keyword,
                                                    @Param("status") InspectionStatus status,
                                                    Pageable pageable);

    @Query(value = """
            SELECT b FROM Blueprint b WHERE b.categoryId = :first
            AND b.inspectionStatus = :status
            """)
    Page<Blueprint> findAllByFirstCategory(@Param("first") Long categoryId, @Param("status") InspectionStatus status, Pageable pageable);

    @Query(value = """
        SELECT b.id, b.blueprintImg, b.creatorName, b.blueprintName, b.standardPrice, b.program FROM Blueprint b
        WHERE
            b.categoryId = :first
            AND b.secondCategory = :second
            AND b.inspectionStatus = :status
            AND b.isDeleted = false
       """
    )
    Page<Blueprint> findAllBySecondCategory(@Param("first") Long firstCategoryId, @Param("second") String secondCategory, @Param("status") InspectionStatus status, Pageable pageable);

    @Query(value = "SELECT count(b) FROM Blueprint b")
    Long countAllBlueprint();

    @Query(value = "SELECT b FROM Blueprint b WHERE b.inspectionStatus = :status")
    Page<Blueprint> findByInspectionStatus(@Param("status") InspectionStatus status, Pageable pageable);

    @Query("SELECT b FROM Blueprint b WHERE b.categoryId = :categoryId AND b.inspectionStatus = :inspectionStatus")
    Page<Blueprint> findByCategoryIdAndStatus(
            @Param("categoryId")   Long categoryId,
            @Param("inspectionStatus") InspectionStatus inspectionStatus,
            Pageable pageable
    );

    @Query(value = "SELECT * FROM blueprint b WHERE b.id = :id", nativeQuery = true)
    Optional<Blueprint> findDeletedById(@Param("id") Long id);
}