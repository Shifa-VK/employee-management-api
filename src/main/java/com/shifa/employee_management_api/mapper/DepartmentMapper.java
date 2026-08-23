package com.shifa.employee_management_api.mapper;

import com.shifa.employee_management_api.dto.request.DepartmentRequest;
import com.shifa.employee_management_api.dto.response.DepartmentResponse;
import com.shifa.employee_management_api.entity.department.Department;
import org.mapstruct.Mapper;

/**
 * Author: Shifa VK
 * Created: 30-07-2026
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toEntity(DepartmentRequest departmentRequest);
    DepartmentResponse toResponse(Department department);
}
