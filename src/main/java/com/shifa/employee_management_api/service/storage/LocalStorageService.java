package com.shifa.employee_management_api.service.storage;

import com.shifa.employee_management_api.config.FileStorageConfig;
import com.shifa.employee_management_api.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Author: Shifa VK
 * Created: 26-07-2026
 */
//@Service
@RequiredArgsConstructor
@Slf4j
public class LocalStorageService implements StorageService{
    private final FileStorageConfig fileStorageConfig;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    @Override
    public String uploadFile(MultipartFile file) {

        validateFile(file);

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(fileStorageConfig.getUploadDir());

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Original filename
            String originalFileName =
                    StringUtils.cleanPath(file.getOriginalFilename());

            // Extract extension
            String extension = "";

            int index = originalFileName.lastIndexOf('.');

            if (index > 0) {
                extension = originalFileName.substring(index);
            }

            // Generate unique filename
            String fileName = UUID.randomUUID() + extension;

            // Save file
            Files.copy(
                    file.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            log.info("File uploaded successfully: {}", fileName);

            return fileName;

        } catch (IOException ex) {

            log.error("Failed to upload file", ex);

            throw new RuntimeException("Could not upload file");
        }
    }

    @Override
    public void deleteFile(String fileName) {

        try {

            Path filePath =
                    Paths.get(fileStorageConfig.getUploadDir())
                            .resolve(fileName);

            Files.deleteIfExists(filePath);

            log.info("Deleted file: {}", fileName);

        } catch (IOException ex) {

            log.error("Failed to delete file", ex);

            throw new RuntimeException("Could not delete file");
        }
    }

    @Override
    public String generatePresignedUrl(String fileName) {
        return "";
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new FileStorageException("File must not be empty.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileStorageException("File size exceeds the maximum limit of 5 MB.");
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !(contentType.equals("image/jpeg")
                        || contentType.equals("image/png")
                        || contentType.equals("image/jpg"))) {

            throw new FileStorageException(
                    "Only JPG, JPEG and PNG images are allowed."
            );
        }
    }
}
