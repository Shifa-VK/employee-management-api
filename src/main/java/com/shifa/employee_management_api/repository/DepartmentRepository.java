package com.shifa.employee_management_api.repository;

import com.shifa.employee_management_api.entity.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Shifa VK
 * Created: 31-07-2026
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
