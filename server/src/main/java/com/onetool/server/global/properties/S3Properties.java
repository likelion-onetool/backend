package com.onetool.server.global.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3Properties {

    public static String BUCKET_NAME;
    public static String BUCKET_INSPECTION_DIRECTORY;
    public static String BUCKET_DETAILS_DIRECTORY;
    public static String BUCKET_THUMBNAIL_DIRECTORY;
    public static String BUCKET_SALES_DIRECTORY;

    public S3Properties(
            @Value("${cloud.aws.s3.bucket}") String bucketName,
            @Value("${cloud.aws.s3.bucket.inspection-directory}") String bucketInspectionDirectory,
            @Value("${cloud.aws.s3.bucket.details-directory}") String bucketDetailsDirectory,
            @Value("${cloud.aws.s3.bucket.thumbnail-directory}") String bucketThumbnailDirectory,
            @Value("${cloud.aws.s3.bucket.sales-directory}") String bucketSalesDirectory
    ) {
        BUCKET_NAME = bucketName;
        BUCKET_INSPECTION_DIRECTORY = bucketInspectionDirectory;
        BUCKET_DETAILS_DIRECTORY = bucketDetailsDirectory;
        BUCKET_THUMBNAIL_DIRECTORY = bucketThumbnailDirectory;
        BUCKET_SALES_DIRECTORY = bucketSalesDirectory;
    }
}
