package com.onetool.server.blueprint.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.onetool.server.blueprint.dto.BlueprintUploadRequest;
import com.onetool.server.blueprint.repository.BlueprintRepository;
import com.onetool.server.global.exception.BaseException;
import com.onetool.server.global.exception.codes.ErrorCode;
import com.onetool.server.global.properties.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BlueprintS3UploadService {

    private final AmazonS3Client amazonS3Client;
    private final S3Properties s3Properties;
    private final BlueprintRepository blueprintRepository;

    public Map<String, String> saveFileToInspection(List<MultipartFile> blueprintFiles) throws IOException {
        if (blueprintFiles.isEmpty()) throw new BaseException(ErrorCode.BLUEPRINT_FILE_NECESSARY);
        Map<String, String> result = new HashMap<>();
        for (MultipartFile file : blueprintFiles) {
            validateFileType(file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            String changedName = changeFileName(s3Properties.getBucketInspectionDirectory());
            amazonS3Client.putObject(new PutObjectRequest(s3Properties.getBucket(), changedName, file.getInputStream(), metadata));
            String url = amazonS3Client.getUrl(s3Properties.getBucket(), changedName).toString();
            result.put(file.getOriginalFilename(), url);
        }
        return result;
    }

    private void validateFileType(MultipartFile file) {
        if (isContentTypeNotAllowed(file.getContentType())) throw new BaseException(ErrorCode.BLUEPRINT_FILE_EXTENSION_NOT_ALLOWED);
    }

    private boolean isContentTypeNotAllowed(String contentType) {
        return !contentType.equals("image/vnd.dwg");
    }

    private String changeFileName(String bucketDirectory) {
        return bucketDirectory +
                "/" +
                UUID.randomUUID().toString();
    }
}