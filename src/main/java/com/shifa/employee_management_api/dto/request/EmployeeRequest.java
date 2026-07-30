package com.shifa.employee_management_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Employee Request")
public class EmployeeRequest {

    @NotBlank(message = "Name is required")
    @Schema(
            description = "Employee Name",
            example = "Shifa VK"
    )
    private String name;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    @Schema(
            description = "Employee Email",
            example = "shifa@gmail.com"
    )
    private String email;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    @NotNull(message = "Phone no is required")
    @Schema(
            description = "Phone Number",
            example = "9876543210"
    )
    private String phone;

    @NotBlank(message = "Department is required")
    @Schema(
            description = "Department",
            example = "Android"
    )
    private String department;

    @Positive(message = "Salary must be greater than zero")
    @Schema(
            description = "Salary",
            example = "75000"
    )
    private Double salary;
}
