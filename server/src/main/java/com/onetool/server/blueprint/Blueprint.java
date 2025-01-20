package com.onetool.server.blueprint;

import com.onetool.server.blueprint.dto.BlueprintRequest;
import com.onetool.server.blueprint.dto.BlueprintUploadRequest;
import com.onetool.server.cart.CartBlueprint;
import com.onetool.server.global.entity.BaseEntity;
import com.onetool.server.order.OrderBlueprint;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blueprint extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="blueprint_name")
    private String blueprintName;

    @Column(name="category_id")
    private Long categoryId;

    @Column(name = "standard_price")
    private Long standardPrice;

    @Column(name = "blueprint_img")
    private String blueprintImg;

    @Column(name = "blueprint_details")
    private String blueprintDetails;

    private String extension;

    private String program;

    private BigInteger hits;

    @Column(name = "sale_price")
    private Long salePrice;

    @Column(name = "sale_expired_date")
    private LocalDateTime saleExpiredDate;

    @Column(name = "creator_name")
    private String creatorName;

    @Column(name = "download_link")
    private String downloadLink;

    @Column(name = "second_category")
    private String secondCategory;

    @Column(name = "inspection_status")
    @Enumerated(EnumType.STRING)
    private InspectionStatus inspectionStatus;

    @OneToMany(mappedBy = "blueprint")
    private List<OrderBlueprint> orderBlueprints = new ArrayList<>();

    @OneToMany(mappedBy = "blueprint")
    private List<CartBlueprint> cartBlueprints = new ArrayList<>();

    @OneToMany(mappedBy = "blueprint")
    private List<BlueprintFile> blueprintFiles = new ArrayList<>();

    @Builder
    public Blueprint(Long id, String blueprintName, Long categoryId, Long standardPrice, String blueprintImg, String blueprintDetails, String extension, String program, BigInteger hits, Long salePrice, LocalDateTime saleExpiredDate, String creatorName, String downloadLink, String secondCategory, InspectionStatus inspectionStatus, List<OrderBlueprint> orderBlueprints, List<CartBlueprint> cartBlueprints) {
        this.id = id;
        this.blueprintName = blueprintName;
        this.categoryId = categoryId;
        this.standardPrice = standardPrice;
        this.blueprintImg = blueprintImg;
        this.blueprintDetails = blueprintDetails;
        this.extension = extension;
        this.program = program;
        this.hits = hits;
        this.salePrice = salePrice;
        this.saleExpiredDate = saleExpiredDate;
        this.creatorName = creatorName;
        this.downloadLink = downloadLink;
        this.secondCategory = secondCategory;
        this.inspectionStatus = inspectionStatus;
        this.orderBlueprints = orderBlueprints;
        this.cartBlueprints = cartBlueprints;
    }

    public void approveBlueprint() {
        this.inspectionStatus = InspectionStatus.PASSED;
    }
  
    public static Blueprint fromRequest(final BlueprintRequest blueprintRequest) {
        return Blueprint.builder()
                .id(blueprintRequest.id())
                .blueprintName(blueprintRequest.blueprintName())
                .categoryId(blueprintRequest.categoryId())
                .standardPrice(blueprintRequest.standardPrice())
                .blueprintImg(blueprintRequest.blueprintImg())
                .blueprintDetails(blueprintRequest.blueprintDetails())
                .extension(blueprintRequest.extension())
                .program(blueprintRequest.program())
                .hits(blueprintRequest.hits())
                .salePrice(blueprintRequest.salePrice())
                .saleExpiredDate(blueprintRequest.saleExpiredDate())
                .creatorName(blueprintRequest.creatorName())
                .downloadLink(blueprintRequest.downloadLink())
                .build();
    }

    public static Blueprint fromUploadRequest(final BlueprintUploadRequest uploadRequest){
        return Blueprint.builder()
                .categoryId(uploadRequest.categoryId())
                .standardPrice(uploadRequest.standardPrice())
                .blueprintDetails(uploadRequest.blueprintDetails())
                .extension(uploadRequest.extension())
                .program(uploadRequest.program())
                .creatorName(uploadRequest.creatorName())
                .build();
    }
}
