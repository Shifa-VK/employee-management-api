package com.shifa.employee_management_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Author: Shifa VK
 * Created: 26-07-2026
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final FileStorageConfig fileStorageConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        Path uploadPath = Paths.get(fileStorageConfig.getUploadDir());

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath.toUri().toString());
    }
}
