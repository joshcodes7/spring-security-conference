package com.example.EmployeeManagementApplication.controller;

import com.example.EmployeeManagementApplication.dto.AuthenticationRequest;
import com.example.EmployeeManagementApplication.dto.AuthenticationResponse;
import com.example.EmployeeManagementApplication.dto.EmployeeDTO;
import com.example.EmployeeManagementApplication.dto.RegisterRequest;
import com.example.EmployeeManagementApplication.model.Employee;
import com.example.EmployeeManagementApplication.model.Project;
import com.example.EmployeeManagementApplication.repository.EmployeeRepository;
import com.example.EmployeeManagementApplication.security.Role;
import com.example.EmployeeManagementApplication.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {


    @Autowired
    private EmployeeRepository registrationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(employeeService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(employeeService.authenticate(request));
    }


    @PostMapping("/employee")
    public ResponseEntity<String> createEmployee(@RequestBody Employee employeeDto) {
        // Check if the user has the role "ADMIN"
//        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
//        }

        employeeDto.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        employeeDto.setRole(Role.ROLE_USER);
        employeeService.saveEmployee(employeeDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("Employee created successfully");
    }


    //@PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/get_employee/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        // Check if the provided ID is valid
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Invalid employee ID");
        }


        Employee employee = employeeService.getEmployeeById(id);

        // Check if the employee exists
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.OK).body("Employee with ID " + id + " does not exist");
        }

        // Get the first name, last name, and project title
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();

        List<Project> projects = employee.getProject();
        List<String> projectTitles = new ArrayList<>();

        for (Project project : projects) {
            projectTitles.add(project.getTitle());
        }

        // Create a DTO to hold the response data
        EmployeeDTO responseDto = new EmployeeDTO(firstName, lastName, projectTitles);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id)
    {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Invalid project ID");
        }
        Employee employee = registrationRepository.findById(id).orElse(null);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.OK).body("Project with ID " + id + " does not exist");
        }

        List<Project> projects = employee.getProject();
        if(projects != null) {
            // Remove the employee from each project
            for (Project project : projects) {
                project.getEmployee().remove(employee);
            }

            // Clear the employee's project set
            employee.getProject().clear();
        }

        registrationRepository.deleteById(id);
        return ResponseEntity.ok("Employee with ID " + id + " deleted");
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @RequestBody Employee employee)
    {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Invalid project ID");
        }
        Employee employee1 = registrationRepository.findById(id).orElse(null);
        if (employee1 == null) {
            return ResponseEntity.status(HttpStatus.OK).body("Project with ID " + id + " does not exist");
        }
        else{

            employee1.setFirstName(employee.getFirstName());
            employee1.setLastName(employee.getLastName());
            employee1.setPassword(passwordEncoder.encode(employee1.getPassword()));
            employee1.setEmail(employee.getEmail());
            employee.setRole(employee.getRole());
            registrationRepository.save(employee1);

        }
        return ResponseEntity.ok("Employee updated successfully");

    }


}

