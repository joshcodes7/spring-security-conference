package com.example.EmployeeManagementApplication.controller;

import com.example.EmployeeManagementApplication.dto.EmployeeDTO;
import com.example.EmployeeManagementApplication.model.Employee;
import com.example.EmployeeManagementApplication.model.LoginResponse;
import com.example.EmployeeManagementApplication.model.Project;
import com.example.EmployeeManagementApplication.repository.EmployeeRepository;
import com.example.EmployeeManagementApplication.security.JwtIssuer;
import com.example.EmployeeManagementApplication.security.UserPrincipal;
import com.example.EmployeeManagementApplication.service.EmployeeService;
import com.example.EmployeeManagementApplication.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {


    @Autowired
    private EmployeeRepository registrationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JwtIssuer jwtIssuer;

    @Autowired
    private EmployeeService employeeService;


    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    private ResponseEntity<String> registerForNew(@RequestBody Employee registration) {

        //if(registrationRepository.findByPassword(passwordEncoder.encode(registration.getPassword()))){}
        Employee existingUser = registrationRepository.findByEmail(registration.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already existing username, use a valid one.");
        }
        System.out.println(registration.getRole());
        System.out.println(registration.getEmail());
        registration.setPassword(passwordEncoder.encode(registration.getPassword()));
        registrationRepository.save(registration);
        return ResponseEntity.ok("Successfully saved");
    }

//    @PostMapping("/login")
//    public com.example.EmployeeManagementApplication.model.LoginResponse loginValidation(@RequestBody @Validated Employee login){
//        var authentication=authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(login.getEmail(),login.getPassword())
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        var principal=(UserPrincipal)authentication.getPrincipal();
//        System.out.println("in reg before role:"+principal.getAuthorities());
//        List<String> roles=principal.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .toList();
//        System.out.println("In reg role:"+roles);
//        var token=jwtIssuer.issue( login.getEmail(),login.getPassword());
//        return com.example.EmployeeManagementApplication.model.LoginResponse.builder()
//                .accessToken(token)
//                .build();
//    }

    @PostMapping("/login")
    private LoginResponse loginValidation(@RequestBody @Validated Employee login) {
        String msg = "";
        Employee employee1 = registrationRepository.findByEmail(login.getEmail());
        if (employee1 != null) {
            String password = login.getPassword();
            String encodedPassword = employee1.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                var token = jwtIssuer.issue(login.getEmail(), login.getPassword());
                Optional<Employee> employeeReg = registrationRepository.findOneByEmailAndPassword(login.getEmail(), encodedPassword);
                return LoginResponse.builder()
                        .accessToken(token).build();
            }

        } else {
            //return new LoginMesage("Email not exits", false);
            return (LoginResponse) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        return null;
    }


    @PostMapping("/employee")
    public ResponseEntity<String> createEmployee(@RequestBody Employee employeeDto) {
        // Check if the user has the role "ADMIN"
//        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
//        }

        employeeDto.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        employeeService.saveEmployee(employeeDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("Employee created successfully");
    }


    @GetMapping("/employee/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        // Check if the provided ID is valid
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Invalid employee ID");
        }

        // Check if the user has the role "USER"
//        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
//        }

        // Retrieve the employee from the database

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

        // Remove the employee from each project
        for (Project project : projects) {
            project.getEmployee().remove(employee);
        }

        // Clear the employee's project set
        employee.getProject().clear();

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
            employee1.setPassword(employee.getPassword());
            employee1.setEmail(employee.getEmail());
            registrationRepository.save(employee1);

        }
        return ResponseEntity.ok("Employee updated successfully");

    }


}

