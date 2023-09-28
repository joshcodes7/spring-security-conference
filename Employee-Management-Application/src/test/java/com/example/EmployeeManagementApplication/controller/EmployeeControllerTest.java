package com.example.EmployeeManagementApplication.controller;

import com.example.EmployeeManagementApplication.dto.EmployeeDTO;
import com.example.EmployeeManagementApplication.model.Employee;
import com.example.EmployeeManagementApplication.model.Project;
import com.example.EmployeeManagementApplication.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

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



}

