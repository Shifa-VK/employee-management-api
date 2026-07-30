package com.shifa.employee_management_api.config.aws;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Author: Shifa VK
 * Created: 27-07-2026
 */
@Getter
@Component
public class AwsProperties {
    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.bucket}")
    private String bucket;
}
