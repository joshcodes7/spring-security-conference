package com.example.EmployeeManagementApplication.controller;

import com.example.EmployeeManagementApplication.dto.EmployeeDTO;
import com.example.EmployeeManagementApplication.model.Employee;
import com.example.EmployeeManagementApplication.model.Project;
import com.example.EmployeeManagementApplication.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    private EmployeeService employeeService = mock(EmployeeService.class);

    private EmployeeController yourController = new EmployeeController();

    @Test
    void testGetEmployeeValidId() {
        // Arrange
        Long validId = 1L;
        Employee mockEmployee = new Employee();
        mockEmployee.setId(validId);
        mockEmployee.setFirstName("John");
        mockEmployee.setLastName("Doe");

        List<Project> projects = new ArrayList<>();
        Project project1 = new Project();
        project1.setTitle("Project A");
        projects.add(project1);

        Project project2 = new Project();
        project2.setTitle("Project B");
        projects.add(project2);

        mockEmployee.setProject(projects);

        when(employeeService.getEmployeeById(validId)).thenReturn(mockEmployee);

        // Act
        ResponseEntity<?> response = yourController.getEmployee(validId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        EmployeeDTO expectedResponse = new EmployeeDTO("John", "Doe", Arrays.asList("Project A", "Project B"));
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testGetEmployeeInvalidId() {
        // Arrange
        Long invalidId = -1L;

        // Act
        ResponseEntity<?> response = yourController.getEmployee(invalidId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid employee ID", response.getBody());
    }

    @Test
    void testGetEmployeeNonexistentId() {
        // Arrange
        Long nonExistentId = 9L;

        when(employeeService.getEmployeeById(nonExistentId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = yourController.getEmployee(nonExistentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee with ID 9 does not exist", response.getBody());
    }


}
