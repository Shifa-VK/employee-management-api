package com.shifa.employee_management_api.dto.response;

import com.shifa.employee_management_api.entity.employee.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private Long id;

    private String name;

    private String email;

    private Role role;
}
