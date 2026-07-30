package com.shifa.employee_management_api.mapper;

import com.shifa.employee_management_api.dto.request.EmployeeRequest;
import com.shifa.employee_management_api.dto.response.EmployeeResponse;
import com.shifa.employee_management_api.entity.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee toEntity(EmployeeRequest request);
    EmployeeResponse toResponse(Employee employee);
    List<EmployeeResponse> toResponseLimit(List<Employee> employeeList);

//    EmployeeResponse toEmployeeResponse(Employee savedEmployee);
}
