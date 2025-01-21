package com.onetool.server.api.blueprint.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.BlueprintFile;
import com.onetool.server.api.blueprint.dto.BlueprintUploadRequest;
import com.onetool.server.api.blueprint.repository.BlueprintFileRepository;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.global.exception.BaseException;
import com.onetool.server.global.exception.codes.ErrorCode;
import com.onetool.server.global.properties.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BlueprintS3UploadService {

    private final AmazonS3Client amazonS3Client;
    private final BlueprintRepository blueprintRepository;
    private final BlueprintFileRepository blueprintFileRepository;

    @Transactional
    public String saveFileToInspection(final BlueprintUploadRequest request,
                                       final List<MultipartFile> files) throws IOException {
        if (files.isEmpty()) {
            throw new BaseException(ErrorCode.BLUEPRINT_FILE_NECESSARY);
        }
        Blueprint blueprint = blueprintRepository.save(Blueprint.fromUploadRequest(request));
        for (MultipartFile file : files) {
            validateFileType(file);
            ObjectMetadata metadata = initMetadata(file);
            String changedName = changeFileName(S3Properties.BUCKET_INSPECTION_DIRECTORY);
            amazonS3Client.putObject(
                    new PutObjectRequest(
                            S3Properties.BUCKET_NAME, changedName, file.getInputStream(), metadata
                    )
            );
            String uploadedUrl = amazonS3Client.getUrl(S3Properties.BUCKET_NAME, changedName).toString();
            BlueprintFile blueprintFile = BlueprintFile.of(changedName, file.getOriginalFilename(), uploadedUrl);
            blueprintFile.update(blueprint);
            blueprintFileRepository.save(blueprintFile);
        }
        return "업로드 완료";
    }

    private ObjectMetadata initMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        return metadata;
    }

    private void validateFileType(MultipartFile file) {
        if (isContentTypeNotAllowed(file.getContentType())){
            throw new BaseException(ErrorCode.BLUEPRINT_FILE_EXTENSION_NOT_ALLOWED);
        }
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