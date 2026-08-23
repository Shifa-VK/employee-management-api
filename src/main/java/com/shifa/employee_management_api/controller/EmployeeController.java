package com.shifa.employee_management_api.controller;

import com.shifa.employee_management_api.dto.response.ApiResponse;
import com.shifa.employee_management_api.dto.request.EmployeeRequest;
import com.shifa.employee_management_api.dto.response.EmployeeResponse;
import com.shifa.employee_management_api.dto.response.PageResponse;
import com.shifa.employee_management_api.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
@Tag(
        name = "Employee Management",
        description = "CRUD APIs for Employee Management"
)
@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /* Save Employee*/
    @Operation(
            summary = "Create Employee",
            description = "Creates a new employee in the database"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Employee created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "An employee with the same email already exists"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation Failed"
            )
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponse>> saveEmployee(@Valid @RequestBody EmployeeRequest request){
        EmployeeResponse savedEmployee =employeeService.saveEmployee(request);
        ApiResponse<EmployeeResponse> response = new ApiResponse<>(
                true,
                HttpStatus.CREATED.value(),
                "Employee Created Successfully",
                savedEmployee,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /*Get All Employees*/
    @Operation(
            summary = "Get All Employees",
            description = "Return all employees"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Employees fetched successfully"
            )
    }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAllEmployees(){
        List<EmployeeResponse> employeeList = employeeService.getAllEmployees();
        ApiResponse<List<EmployeeResponse>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Employees fetched successfully",
                employeeList,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /* Get employye by id*/
    @GetMapping("/{id}")
    @Operation(
            summary = "Get Employee by id",
            description = "Return employee details by id"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Employee Found"
        ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR', 'USER')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(@PathVariable Long id){
        EmployeeResponse employeeResponse = employeeService.getEmployeeById(id);
        ApiResponse<EmployeeResponse> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "success",
                employeeResponse,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /* Delete Employee by id*/
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Employee",
            description = "Deletes employee by ID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Employee not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable Long id){
        employeeService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(
                        true,
                        HttpStatus.OK.value(),
                        "Deleted Successfully",
                        null,
                        LocalDateTime.now()
                ));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Employee",
            description = "Updates employee details"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Employee not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request
    ){
        EmployeeResponse response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Updated successfully",
                response,
                LocalDateTime.now()
        ));
    }

    /*search employee*/
    @GetMapping("/search")
    @Operation(
            summary = "Search Employees",
            description = "Search employees with pagination and sorting"
    )
    public ResponseEntity<ApiResponse<PageResponse<EmployeeResponse>>> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam String search){
        PageResponse<EmployeeResponse> employees = employeeService.getEmployees(
                page,
                size,
                sortBy,
                sortDir,
                search
        );
        ApiResponse<PageResponse<EmployeeResponse>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Employees Fetched successfully",
                employees,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/{id}/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        EmployeeResponse response = employeeService.uploadProfileImage(id, file);

        return ResponseEntity.ok(
                ApiResponse.<EmployeeResponse>builder()
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .message("Profile image uploaded successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/{id}/profile-image")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<ApiResponse<Void>> deleteProfileImage(
            @PathVariable Long id) {

        employeeService.deleteProfileImage(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .message("Profile image deleted successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
