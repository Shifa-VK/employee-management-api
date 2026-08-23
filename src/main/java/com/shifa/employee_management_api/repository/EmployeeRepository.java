package com.shifa.employee_management_api.repository;

import com.shifa.employee_management_api.entity.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    Page<Employee> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );
}
