package com.shifa.employee_management_api.dto.request;

import com.shifa.employee_management_api.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Schema(example = "Shifa VK")
    private String name;

    @Email(message = "Invalid Email")
    @NotBlank(message = "Email is required")
    @Schema(example = "shifa@gmail.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must contain uppercase, lowercase, number and special character"
    )
    @Schema(example = "Password@123")
    private String password;

    @Schema(example = "USER")
    private Role role;
}
