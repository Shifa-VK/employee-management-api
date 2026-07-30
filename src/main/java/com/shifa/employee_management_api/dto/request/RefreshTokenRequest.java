package com.shifa.employee_management_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Author: Shifa VK
 * Created: 25-07-2026
 */
@Data
@Schema(description = "Refresh Token Request")
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token is required")
    @Schema(description = "JWT Refresh Token")
    private String refreshToken;
}
