package com.shifa.employee_management_api.mapper;

import com.shifa.employee_management_api.dto.request.EmployeeRequest;
import com.shifa.employee_management_api.dto.response.EmployeeResponse;
import com.shifa.employee_management_api.entity.employee.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(target = "department", ignore = true)
    Employee toEntity(EmployeeRequest request);

    @Mapping(target = "department", source = "department")
    EmployeeResponse toResponse(Employee employee);
}
