package com.example.EmployeeManagementApplication.repository;

import com.example.EmployeeManagementApplication.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Employee findByPassword(String password);
    Employee findByEmail(String email);

    Optional<Employee> findOneByEmailAndPassword(String email, String password);
}
