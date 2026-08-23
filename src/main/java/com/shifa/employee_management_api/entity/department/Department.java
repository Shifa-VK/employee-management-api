package com.shifa.employee_management_api.entity.department;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shifa.employee_management_api.entity.employee.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: Shifa VK
 * Created: 30-07-2026
 */
@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(
            mappedBy = "department",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Employee> employeeList;
}
