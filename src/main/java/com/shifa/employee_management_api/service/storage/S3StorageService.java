package com.shifa.employee_management_api.service.storage;

import com.shifa.employee_management_api.config.aws.AwsProperties;
import com.shifa.employee_management_api.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

/**
 * Author: Shifa VK
 * Created: 27-07-2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class S3StorageService implements StorageService{
    private final S3Client s3Client;
    private final AwsProperties awsProperties;
    private final S3Presigner s3Presigner;
    @Override
    public String uploadFile(MultipartFile file) {
        try {

            String originalFileName = file.getOriginalFilename();

            String extension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID() + extension;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsProperties.getBucket())
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes())
            );

            log.info("Uploaded file to S3: {}", fileName);

            return fileName;

        } catch (IOException e) {
            throw new FileStorageException("Failed to upload file to S3");
        }
    }

    @Override
    public void deleteFile(String fileName) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(awsProperties.getBucket())
                .key(fileName)
                .build();

        s3Client.deleteObject(request);

        log.info("Deleted file from S3: {}", fileName);
    }

    @Override
    public String generatePresignedUrl(String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(awsProperties.getBucket())
                .key(fileName)
                .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(getObjectRequest)
                        .build();


        return s3Presigner
                .presignGetObject(presignRequest)
                .url()
                .toString();
    }
}
