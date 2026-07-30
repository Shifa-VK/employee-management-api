package com.shifa.employee_management_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * Author: Shifa VK
 * Created: 25-07-2026
 */
@Data
@Builder
@Schema(description = "Refresh Token Response")
public class RefreshTokenResponse {
    @Schema(description = "New JWT Access Token")
    private String accessToken;

    @Builder.Default
    @Schema(example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(example = "900000")
    private Long expiresIn;
}
