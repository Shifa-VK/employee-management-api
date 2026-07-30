package com.shifa.employee_management_api.service;

import com.shifa.employee_management_api.dto.request.EmployeeRequest;
import com.shifa.employee_management_api.dto.response.EmployeeResponse;
import com.shifa.employee_management_api.dto.response.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface EmployeeService {

    /* Create employee*/
    EmployeeResponse saveEmployee(EmployeeRequest request);

    /* Get all employees*/
    List<EmployeeResponse> getAllEmployees();

    /* Get Employee by Id*/
    EmployeeResponse getEmployeeById(Long Id);

    /* Delete Employee by Id*/
    void deleteById(Long Id);

    /*Update Employee*/
    EmployeeResponse updateEmployeeResponse(Long Id, EmployeeRequest request);

    /*Search Employee*/
    PageResponse<EmployeeResponse> getEmployees(
            int page,
            int size,
            String sortBy,
            String sortDir,
            String search
    );

    /*Upload profile image*/
    EmployeeResponse uploadProfileImage(Long id, MultipartFile file);

    /*Delete profile image*/
    void deleteProfileImage(Long id);
}
