package com.epam.dimazak.appliances.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee extends User {

    private String department;

    public Employee(Long id, String name, String email, String password, String department, boolean isEnabled) {
        super(id, name, email, password, Role.EMPLOYEE, isEnabled);
        this.department = department;
    }
}