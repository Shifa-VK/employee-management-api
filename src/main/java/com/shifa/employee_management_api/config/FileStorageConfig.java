package com.shifa.employee_management_api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Shifa VK
 * Created: 26-07-2026
 */
@Data
@Configuration
public class FileStorageConfig {
    @Value("${file.upload-dir}")
    private String uploadDir;
}
