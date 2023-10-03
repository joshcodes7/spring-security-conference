package com.example.EmployeeManagementApplication.controller;

import com.example.EmployeeManagementApplication.dto.EmployeeDTO;
import com.example.EmployeeManagementApplication.model.Employee;
import com.example.EmployeeManagementApplication.model.Project;
import com.example.EmployeeManagementApplication.repository.EmployeeRepository;
import com.example.EmployeeManagementApplication.security.Role;
import com.example.EmployeeManagementApplication.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private EmployeeController employeeController;

    @MockBean
    private EmployeeRepository registrationRepository;

    @Test
    public void testGetEmployeeValidId() {

        Long validId = 1L;
        Employee employee = new Employee();
        employee.setId(validId);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        List<Project> projects = new ArrayList<>();
        Project project = new Project();
        project.setTitle("Project A");
        projects.add(project);

        employee.setProject(projects);

        when(employeeService.getEmployeeById(validId)).thenReturn(employee);

        // Acting
        ResponseEntity<?> responseEntity = employeeController.getEmployee(validId);

        // Assert - checking with mock db expected and retrived?
        assertEquals(200, responseEntity.getStatusCodeValue());

        EmployeeDTO responseDto = (EmployeeDTO) responseEntity.getBody();
        assertNotNull(responseDto);
        assertEquals("John", responseDto.getFirstName());
        assertEquals("Doe", responseDto.getLastName());
        assertEquals(1, responseDto.getTitle().size());
        assertEquals("Project A", responseDto.getTitle().get(0));
    }

    @Test
    public void testGetEmployeeInvalidId() {

        Long invalidId = -1L;

        // Act
        ResponseEntity<?> responseEntity = employeeController.getEmployee(invalidId);

        // Assert
        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("Invalid employee ID", responseEntity.getBody());
    }

    @Test
    public void testGetEmployeeNonExistentId() {

        Long nonExistentId = 9L;
        when(employeeService.getEmployeeById(nonExistentId)).thenReturn(null);

        // Act
        ResponseEntity<?> responseEntity = employeeController.getEmployee(nonExistentId);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Employee with ID 9 does not exist", responseEntity.getBody());
    }

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    public void testCreateEmployee() throws Exception {
//
//        Employee employeeDto = new Employee();
//        employeeDto.setFirstName("John");
//        employeeDto.setLastName("Doe");
//        employeeDto.setEmail("john.doe@example.com");
//        employeeDto.setPassword("password");
//        employeeDto.setRole(Role.ROLE_USER);
//
//        // Create a request content as JSON
//        String requestJson = new ObjectMapper().writeValueAsString(employeeDto);
//
//        // Perform the POST request
//        mockMvc.perform(post("/employee")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isCreated())
//                .andExpect(content().string("Employee created successfully"));
//
//        // Verify that employeeService.saveEmployee() was called with the provided employeeDto
//        verify(employeeService, times(1)).saveEmployee(any(Employee.class));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void testDeleteEmployee() throws Exception {
//        Long validEmployeeId = 2L;
//
//        Employee mockEmployee = new Employee();
//        mockEmployee.setId(validEmployeeId);
//        when(registrationRepository.findById(validEmployeeId)).thenReturn(Optional.of(mockEmployee));
//
//        mockMvc.perform(delete("/employee/{id}", validEmployeeId))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Employee with ID " + validEmployeeId + " deleted"));
//
//        // Verify that deleteById was called with the correct ID
//        verify(registrationRepository, times(1)).deleteById(validEmployeeId);
//    }
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        //employeeController = new EmployeeController(passwordEncoder);
    }

    @Test
    public void testCreateEmployee() {
        // Create an example Employee object
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPassword("password123");

        // Call the controller method directly
        ResponseEntity<String> responseEntity = employeeController.createEmployee(employee);

        // Check the response
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Employee created successfully", responseEntity.getBody());
    }

//    @Test
//    void testDeleteProject() {
//        Long employeeId = 1L;
//
//        Employee mockEmployee = new Employee();
//        mockEmployee.setId(employeeId);
//
//        Project mockProject1 = new Project();
//        mockProject1.setId(1L);
//        mockProject1.setEmployee(new ArrayList<>(Collections.singletonList(mockEmployee)));
//
//        mockEmployee.setProject(Collections.singletonList(mockProject1));
//
//        when(registrationRepository.findById(employeeId)).thenReturn(Optional.of(mockEmployee));
//
//        ResponseEntity<String> response = employeeController.deleteProject(employeeId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Employee with ID " + employeeId + " deleted", response.getBody());
//
//        verify(registrationRepository, times(1)).deleteById(employeeId);
//    }

    @Test
    void testUpdateEmployee() {
        Long employeeId = 1L;
        Employee updatedEmployee = new Employee();
        updatedEmployee.setFirstName("John");
        updatedEmployee.setLastName("Doe");
        updatedEmployee.setEmail("john.doe@example.com");

        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setFirstName("Jane");
        existingEmployee.setLastName("Doe");
        existingEmployee.setEmail("jane.doe@example.com");

        when(registrationRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        ResponseEntity<String> response = employeeController.updateEmployee(employeeId, updatedEmployee);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee updated successfully", response.getBody());

        verify(registrationRepository, times(1)).save(existingEmployee);
        assertEquals("John", existingEmployee.getFirstName());
        assertEquals("Doe", existingEmployee.getLastName());
        assertEquals("john.doe@example.com", existingEmployee.getEmail());
    }

    @Test
    public void testDeleteProject() {

        Long employeeId = 2L;
        Employee testEmployee = new Employee();
        testEmployee.setId(employeeId);

        Mockito.when(registrationRepository.findById(employeeId)).thenReturn(Optional.of(testEmployee));

        // Call the deleteProject method
        ResponseEntity<String> response = employeeController.deleteProject(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee with ID " + employeeId + " deleted", response.getBody());
    }

}

