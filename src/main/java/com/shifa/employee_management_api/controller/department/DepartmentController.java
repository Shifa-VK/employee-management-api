package com.shifa.employee_management_api.controller.department;

import com.shifa.employee_management_api.dto.request.DepartmentRequest;
import com.shifa.employee_management_api.dto.response.ApiResponse;
import com.shifa.employee_management_api.dto.response.DepartmentResponse;
import com.shifa.employee_management_api.dto.response.EmployeeResponse;
import com.shifa.employee_management_api.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Shifa VK
 * Created: 30-07-2026
 */

@Tag(
        name = "Department Management",
        description = "CRUD APIs for Department Management"
)
@RestController
@RequestMapping("api/v1/department")
@AllArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Department created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Department with the same name already exists"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation Failed"
            )
    }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> saveDepartment(@Valid @RequestBody DepartmentRequest request){
        DepartmentResponse response = departmentService.saveDepartment(request);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        HttpStatus.CREATED.value(),
                        "Department Created Successfully",
                        response,
                        LocalDateTime.now()
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Department",
            description = "Deletes department by ID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access Denied. You don't have permission to perform this action."
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteDepartment(@PathVariable Long id){
        departmentService.deleteDepartment(id);
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
            summary = "Update Department",
            description = "Updates department details"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Department not found with id")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> UpdateDepartment(@Valid @PathVariable Long id, @RequestBody DepartmentRequest request){
        DepartmentResponse response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        HttpStatus.OK.value(),
                        "Updated successfully",
                        response,
                        LocalDateTime.now()
                ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(
            @PathVariable Long id
    ){
        DepartmentResponse response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        HttpStatus.OK.value(),
                        "Department fetched successfully",
                        response,
                        LocalDateTime.now()
                )
        );
    }

    @GetMapping("/{id}/employees")
    @Operation(
            summary = "Get Employees By Department",
            description = "Returns all employees belonging to a department"
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getEmployeesByDepartment(
            @PathVariable Long id) {

        List<EmployeeResponse> response =
                departmentService.getEmployeesByDepartment(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        HttpStatus.OK.value(),
                        "Employees fetched successfully",
                        response,
                        LocalDateTime.now()
                )
        );
    }


    /*Get All Departments*/
    @Operation(
            summary = "Get All Departments",
            description = "Return all departments"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Departments fetched successfully"
            )
    }
    )
    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments(){
        List<DepartmentResponse> employeeList = departmentService.getAllDepartments();
        ApiResponse<List<DepartmentResponse>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Departments fetched successfully",
                employeeList,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
