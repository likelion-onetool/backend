package com.onetool.server.global.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class S3Properties {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.bucket.inspection-directory}")
    private String bucketInspectionDirectory;

    @Value("${cloud.aws.s3.bucket.details-directory}")
    private String bucketDetailsDirectory;

    @Value("${cloud.aws.s3.bucket.thumbnail-directory}")
    private String bucketThumbnailDirectory;

    @Value("${cloud.aws.s3.bucket.sales-directory}")
    private String bucketSalesDirectory;
}
