package com.shifa.employee_management_api.service.impl;

import com.shifa.employee_management_api.dto.request.EmployeeRequest;
import com.shifa.employee_management_api.dto.response.EmployeeResponse;
import com.shifa.employee_management_api.dto.response.PageResponse;
import com.shifa.employee_management_api.entity.Employee;
import com.shifa.employee_management_api.exception.DuplicateEmailException;
import com.shifa.employee_management_api.exception.EmployeeNotFoundException;
import com.shifa.employee_management_api.exception.FileStorageException;
import com.shifa.employee_management_api.mapper.EmployeeMapper;
import com.shifa.employee_management_api.repository.EmployeeRepository;
import com.shifa.employee_management_api.service.EmployeeService;
import com.shifa.employee_management_api.service.storage.StorageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final StorageService storageService;
    public EmployeeResponse saveEmployee(EmployeeRequest request){
        log.info("Creating employee with email: {}", request.getEmail());
        if (employeeRepository.existsByEmail(request.getEmail())){
            log.error("Employee already exists with email: {}", request.getEmail());
            throw new DuplicateEmailException("Employee already exist with same email: "+ request.getEmail());
        }
        Employee employee = employeeMapper.toEntity(request);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with id: {}", savedEmployee.getId());
        return buildEmployeeResponse(savedEmployee);
//        return employeeMapper.toResponse(savedEmployee);
    }

    /* Get all employees*/
    public List<EmployeeResponse> getAllEmployees(){
        log.info("Fetching all employees");
        List<Employee> employeeList = employeeRepository.findAll();
        log.info("Total employees fetched: {}", employeeList.size());
        return employeeList.stream()
                .map(this::buildEmployeeResponse)
                .toList();
        //return employeeMapper.toResponseLimit(employeeList);
    }

    /* Get Employee by Id*/
    public EmployeeResponse getEmployeeById(Long id){
        log.info("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Employee not found with id: {}", id);

                    return new EmployeeNotFoundException(
                            "Employee not found with id: " + id);
                });
        return buildEmployeeResponse(employee);
        //return employeeMapper.toResponse(employee);
    }

    /* Delete Employee by Id*/
    public void deleteById(Long id){
        log.info("Deleting employee with id: {}", id);
        employeeRepository.findById(id)
                .orElseThrow(()->{
                    log.warn("Employee not found with id: {}", id);
                    return new EmployeeNotFoundException("Employee not found with id: " + id);
                });
        employeeRepository.deleteById(id);
        log.info("Employee deleted successfully with id: {}", id);
    }

    /*Update Employee*/
    public EmployeeResponse updateEmployeeResponse(Long id, EmployeeRequest request){
        log.info("Updating employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("Employee not found with id: {}", id);
                    return new EmployeeNotFoundException("Employee not found with ID" + id) ;
                }
                );
        if(employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)){
            log.error("Duplicate email found while updating employee: {}",
                    request.getEmail());
            throw new DuplicateEmailException("Employee already exists with the same email: "+ request.getEmail());
        }
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setSalary(request.getSalary());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        log.info("Employee updated successfully with id: {}", id);
        return buildEmployeeResponse(employeeRepository.save(employee));
        //return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Override
    public PageResponse<EmployeeResponse> getEmployees(
            int page,
            int size,
            String sortBy,
            String sortDir,
            String search) {
        log.info(
                "Fetching employees - page: {}, size: {}, sortBy: {}, sortDir: {}, search: {}",
                page,
                size,
                sortBy,
                sortDir,
                search
        );
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employeePage;
        if (search == null || search.isBlank()){
            employeePage = employeeRepository.findAll(pageable);
        }
        else {
            employeePage = employeeRepository.findByNameContainingIgnoreCase(search, pageable);
        }
        List<EmployeeResponse> employees = employeePage
                .getContent()
                .stream()
                .map(this::buildEmployeeResponse)
                .toList();
        /*if use LocalStorageService uncomment it*/
        /*List<EmployeeResponse> employees = employeePage
                .getContent()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();*/
        log.info("Employees fetched successfully. Total records: {}",
                employeePage.getTotalElements());
        return PageResponse.<EmployeeResponse>builder()
                .items(employees)
                .page(employeePage.getNumber())
                .size(employeePage.getSize())
                .totalElements(employeePage.getTotalElements())
                .first(employeePage.isFirst())
                .last(employeePage.isLast())
                .hasNext(employeePage.hasNext())
                .hasPrevious(employeePage.hasPrevious())
                .build();
    }

    @Override
    public EmployeeResponse uploadProfileImage(Long id, MultipartFile file) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}" , id);
                    return   new EmployeeNotFoundException("Employee not found with id: " + id);
                });

        // Delete old image if it exists
        if (employee.getProfileImage() != null &&
                !employee.getProfileImage().isBlank()) {

            storageService.deleteFile(employee.getProfileImage());
        }

        // Upload new image
        String fileName = storageService.uploadFile(file);

        // Save filename
        employee.setProfileImage(fileName);

        Employee savedEmployee = employeeRepository.save(employee);

        /* if you use S3StorageService*/
        return buildEmployeeResponse(savedEmployee);
        /* if you use LocalStorageService*/
//        return employeeMapper.toResponse(savedEmployee);
    }

    @Override
    public void deleteProfileImage(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id));

        if (employee.getProfileImage() == null || employee.getProfileImage().isBlank()) {
            throw new FileStorageException("Employee does not have a profile image.");
        }

        storageService.deleteFile(employee.getProfileImage());

        employee.setProfileImage(null);

        employeeRepository.save(employee);

        log.info("Profile image deleted for employee {}", id);
    }

    private EmployeeResponse buildEmployeeResponse(Employee employee){
        EmployeeResponse response = employeeMapper.toResponse(employee);
        if (employee.getProfileImage() != null && !employee.getProfileImage().isBlank()){
            response.setProfileImageUrl(
                    storageService.generatePresignedUrl(employee.getProfileImage())
            );
        }
        return response;
    }
}
