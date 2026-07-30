package com.shifa.employee_management_api.service.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Shifa VK
 * Created: 26-07-2026
 */
public interface StorageService {
    /**
     * Upload file and return stored file name.
     */
    String uploadFile(MultipartFile file);

    /**
     * Delete stored file.
     */
    void deleteFile(String fileName);

    /**
     * Generate pre signed url
     */
    String generatePresignedUrl(String fileName);
}
