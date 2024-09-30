package com.onetool.server.blueprint.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onetool.server.global.exception.BaseException;
import com.onetool.server.global.exception.codes.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BlueprintS3UploadService {

    private final AmazonS3Client amazonS3Client;

    //검수받을 도면 버킷
    @Value("${cloud.aws.s3.bucket.inspection}")
    private String inspectionBucket;

    //검수 완료 후 판매 가능한 도면 버킷
    @Value("${cloud.aws.s3.bucket.sales}")
    private String salesBucket;

    //도면 상세페이지 이미지 버킷
    @Value("${cloud.aws.s3.bucket.details}")
    private String detailsBucket;


    // TODO : key(파일 명) 생성전략은 차차 생각해보겠음

    public String saveFileToInspection(MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) throw new BaseException(ErrorCode.BLUEPRINT_FILE_NECESSARY);
        if (isContentTypeNotAllowed(multipartFile.getContentType())) throw new BaseException(ErrorCode.BLUEPRINT_FILE_EXTENSION_NOT_ALLOWED);

        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        log.info("type : {}", multipartFile.getContentType());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3Client.putObject(inspectionBucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3Client.getUrl(inspectionBucket, originalFilename).toString();
    }

    private boolean isContentTypeNotAllowed(String contentType) {
        return !contentType.equals("image/vnd.dwg");
    }

    public String saveFileToSales(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3Client.putObject(salesBucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3Client.getUrl(salesBucket, originalFilename).toString();
    }

    public String saveFileToDetails(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3Client.putObject(detailsBucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3Client.getUrl(detailsBucket, originalFilename).toString();
    }
}