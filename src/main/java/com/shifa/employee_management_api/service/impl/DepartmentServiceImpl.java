package com.shifa.employee_management_api.service.impl;

import com.shifa.employee_management_api.dto.request.DepartmentRequest;
import com.shifa.employee_management_api.dto.response.DepartmentResponse;
import com.shifa.employee_management_api.dto.response.EmployeeResponse;
import com.shifa.employee_management_api.entity.department.Department;
import com.shifa.employee_management_api.entity.employee.Employee;
import com.shifa.employee_management_api.exception.BadRequestException;
import com.shifa.employee_management_api.exception.DuplicateException;
import com.shifa.employee_management_api.exception.NotFoundException;
import com.shifa.employee_management_api.mapper.DepartmentMapper;
import com.shifa.employee_management_api.mapper.EmployeeMapper;
import com.shifa.employee_management_api.repository.DepartmentRepository;
import com.shifa.employee_management_api.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Shifa VK
 * Created: 30-07-2026
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;
    private final EmployeeMapper employeeMapper;
    @Override
    public DepartmentResponse saveDepartment(DepartmentRequest request) {
        log.info("Creating Department with name: {}", request.getName());
        if(repository.existsByName(request.getName())){
            log.info("Department already exists with email {}", request.getName());
            throw new DuplicateException("Department already exists with same name: "+ request.getName());
        }
        Department department = mapper.toEntity(request);
        Department savedDepartment = repository.save(department);
        log.info("Department created with id: {}", savedDepartment.getId());
        return mapper.toResponse(savedDepartment);
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        log.info("Fetching department with id: {}", id);

        Department department = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Department not found with id: {}", id);
                    return new NotFoundException("Department not found with id: " + id);
                });
        return mapper.toResponse(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        log.info("Deleting department with id: {}", id);
        Department department = repository.findById(id)
                .orElseThrow(()->{
                    log.warn("Department not found with id: {}", id);
                    return new NotFoundException("Department not found with id: " + id);
                });
        if (!department.getEmployeeList().isEmpty()) {
            throw new BadRequestException(
                    "Cannot delete department because employees are assigned."
            );
        }
        repository.deleteById(id);
        log.info("Department deleted successfully with id: {}", id);
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        log.info("Deleting department with id: {}", id);
        Department department = repository.findById(id).orElseThrow(() -> {
                    log.warn("Department not found with id: {}", id);
                    return new NotFoundException("Department not found with id: " + id);
                }
        );
        if (repository.existsByNameAndIdNot(department.getName(), department.getId())){
            log.error("Duplicate department found while updating department: {}",
                    request.getName());
            throw new DuplicateException("Department already exists with the same email: "+ request.getName());
        }
        department.setName(request.getName());
        Department savedDepartment = repository.save(department);
        log.info("Department updated successfully with id: {}", id);
        return mapper.toResponse(savedDepartment);
    }

    /*Fetching employees for department id*/
    @Override
    public List<EmployeeResponse> getEmployeesByDepartment(Long departmentId) {
        log.info("Fetching employees for department id: {}", departmentId);

        Department department = repository.findById(departmentId)
                .orElseThrow(() -> {
                    log.warn("Department not found with id: {}", departmentId);
                    return new NotFoundException(
                            "Department not found with id: " + departmentId);
                });

        return department.getEmployeeList()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        log.info("Fetching all departments");
        List<Department> departmentList = repository.findAll();
        log.info("Total department fetched: {}", departmentList.size());
        return departmentList.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
