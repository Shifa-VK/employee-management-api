package com.shifa.employee_management_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Author: Shifa VK
 * Created: 30-07-2026
 */
@Data
public class DepartmentRequest {
    @Schema(
            name = "Department Name",
            example = "Computer Science"
    )
    @NotBlank(message = "Name is required")
    private String name;
}
