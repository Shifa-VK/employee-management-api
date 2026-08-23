package com.shifa.employee_management_api.service;

import com.shifa.employee_management_api.dto.request.DepartmentRequest;
import com.shifa.employee_management_api.dto.response.DepartmentResponse;
import com.shifa.employee_management_api.dto.response.EmployeeResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Shifa VK
 * Created: 30-07-2026
 */

public interface DepartmentService {
    DepartmentResponse saveDepartment(DepartmentRequest request);
    DepartmentResponse getDepartmentById(Long id);
    void deleteDepartment(Long id);
    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);
    List<EmployeeResponse> getEmployeesByDepartment(Long departmentId);
    List<DepartmentResponse> getAllDepartments();
}
