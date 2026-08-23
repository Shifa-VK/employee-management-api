package com.shifa.employee_management_api.dto.response;

import com.shifa.employee_management_api.entity.employee.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "JWT Authentication Response")
@Builder
public class JwtResponse {
    @Schema(description = "JWT Access Token")
    private String accessToken;

    @Schema(description = "JWT Refresh Token")
    private String refreshToken;

    @Builder.Default
    @Schema(example = "Bearer")
    private String tokenType = "Bearer";;

    @Schema(example = "900000")
    private Long expiresIn;

    @Schema(example = "Shifa VK")
    private String name;

    @Schema(example = "shifa@gmail.com")
    private String email;

    @Schema(example = "ADMIN")
    private Role role;
}
