package com.onetool.server.api.blueprint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onetool.server.api.blueprint.dto.request.BlueprintRequest;
import com.onetool.server.api.blueprint.dto.request.BlueprintUpdateRequest;
import com.onetool.server.api.cart.CartBlueprint;
import com.onetool.server.global.entity.BaseEntity;
import com.onetool.server.api.order.OrderBlueprint;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table
        (indexes = {
        @Index(name = "idx_blueprint_second_category", columnList = "categoryId, secondCategory")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Blueprint SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@Builder
@AllArgsConstructor
public class Blueprint extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String blueprintName;
    private Long categoryId;
    private Long standardPrice;
    private String blueprintImg;
    private String blueprintDetails;
    private String extension;
    private String program;
    private BigInteger hits = BigInteger.ZERO;
    private Long salePrice;
    private LocalDateTime saleExpiredDate;
    private String creatorName;
    private String downloadLink;
    private String secondCategory;

    @ColumnDefault("'NONE'") // default
    @Enumerated(EnumType.STRING)
    private InspectionStatus inspectionStatus = InspectionStatus.NONE;

    @Builder.Default
    @OneToMany(mappedBy = "blueprint")
    @JsonIgnore
    private List<OrderBlueprint> orderBlueprints = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "blueprint")
    @JsonIgnore
    private List<CartBlueprint> cartBlueprints = new ArrayList<>();

    @Column(nullable = false)
    private boolean isDeleted = false;

    private String detailImage;

    public void updateBlueprint(BlueprintUpdateRequest request) {
        Optional.ofNullable(request.blueprintName()).ifPresent(name -> this.blueprintName = name);
        Optional.ofNullable(request.categoryId()).ifPresent(id -> this.categoryId = id);
        Optional.ofNullable(request.standardPrice()).ifPresent(price -> this.standardPrice = price);
        Optional.ofNullable(request.blueprintImg()).ifPresent(img -> this.blueprintImg = img);
        Optional.ofNullable(request.blueprintDetails()).ifPresent(details -> this.blueprintDetails = details);
        Optional.ofNullable(request.extension()).ifPresent(ext -> this.extension = ext);
        Optional.ofNullable(request.program()).ifPresent(prog -> this.program = prog);
        Optional.ofNullable(request.hits()).ifPresent(hits -> this.hits = BigInteger.valueOf(hits));
        Optional.ofNullable(request.salePrice()).ifPresent(salePrice -> this.salePrice = salePrice);
        Optional.ofNullable(request.saleExpiredDate()).ifPresent(expiredDate -> this.saleExpiredDate = expiredDate);
        Optional.ofNullable(request.creatorName()).ifPresent(creator -> this.creatorName = creator);
        Optional.ofNullable(request.downloadLink()).ifPresent(link -> this.downloadLink = link);
        Optional.of(request.isDeleted()).ifPresent(deleted -> this.isDeleted = deleted);
        Optional.ofNullable(request.detailImage()).ifPresent(detailImg -> this.detailImage = detailImg);
    }

    public void approveBlueprint() {
        this.inspectionStatus = InspectionStatus.PASSED;
    }

    public static Blueprint fromRequest(final BlueprintRequest blueprintRequest) {
        return Blueprint.builder()
                .blueprintName(blueprintRequest.blueprintName())
                .categoryId(blueprintRequest.categoryId())
                .standardPrice(blueprintRequest.standardPrice())
                .blueprintImg(blueprintRequest.blueprintImg())
                .blueprintDetails(blueprintRequest.blueprintDetails())
                .extension(blueprintRequest.extension())
                .program(blueprintRequest.program())
                .salePrice(blueprintRequest.salePrice())
                .saleExpiredDate(blueprintRequest.saleExpiredDate())
                .creatorName(blueprintRequest.creatorName())
                .downloadLink(blueprintRequest.downloadLink())
                .detailImage(blueprintRequest.detailImage())
                .build();
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

}