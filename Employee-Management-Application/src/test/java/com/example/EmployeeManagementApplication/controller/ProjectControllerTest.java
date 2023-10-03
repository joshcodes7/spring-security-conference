package com.example.EmployeeManagementApplication.controller;

import com.example.EmployeeManagementApplication.dto.ProjectDTO;
import com.example.EmployeeManagementApplication.dto.ProjectResponse;
import com.example.EmployeeManagementApplication.model.Employee;
import com.example.EmployeeManagementApplication.model.Project;
import com.example.EmployeeManagementApplication.repository.EmployeeRepository;
import com.example.EmployeeManagementApplication.repository.ProjectRepository;
import com.example.EmployeeManagementApplication.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

//    private ProjectService projectService = mock(ProjectService.class);
//    private ProjectController projectController = new ProjectController();

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @Mock
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;


    @Test
    void testGetProject() {
        Long projectId = 1L;
        String title = "Sample Project";
        Long managerId = 100L;
        List<String> assignees = Arrays.asList("John Doe", "Jane Doe");

        Project project = new Project();
        project.setId(projectId);
        project.setTitle(title);
        project.setManagerId(managerId);

        when(projectService.getProjectById(projectId)).thenReturn(project);
        when(projectService.getAssigneesNames(projectId)).thenReturn(assignees);

        ResponseEntity<?> responseEntity = projectController.getProject(projectId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ProjectDTO);

        ProjectDTO responseDto = (ProjectDTO) responseEntity.getBody();
        assertEquals(title, responseDto.getTitle());
        assertEquals(managerId, responseDto.getManagerId());
        assertEquals(assignees, responseDto.getAssigneeName());

        // Verify that projectService methods were called
        verify(projectService, times(1)).getProjectById(projectId);
        verify(projectService, times(1)).getAssigneesNames(projectId);
    }

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        employeeRepository = mock(EmployeeRepository.class);
        //projectController = new ProjectController(projectRepository, employeeRepository);
    }

    @Test
    void testCreateProject() {
        // Arrange
        Long managerId = 101L;
        Long employeeId = 201L;

        Employee manager = new Employee(managerId, "John", "Doe", "john.doe@example.com");
        Project project = new Project(managerId);
        project.getEmployee().add(new Employee(employeeId, "Jane", "Doe", "jane.doe@example.com"));

        when(employeeRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(new Employee(employeeId, "Jane", "Doe", "jane.doe@example.com")));

        // Act
        ResponseEntity<String> responseEntity = projectController.createProject(project);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Project created successfully", responseEntity.getBody());
    }

    @Test
    void testGetProjectsByManager() {
        Long managerId = 1L;

        // Create a sample list of projects
        List<Project> sampleProjects = new ArrayList<>();
        sampleProjects.add(new Project(1L, "Project 1"));
        sampleProjects.add(new Project(2L, "Project 2"));

        // Create a sample list of ProjectResponses
        List<ProjectDTO> expectedResponses = new ArrayList<>();
        expectedResponses.add(new ProjectDTO("Project 1", 1L, Arrays.asList("Assignee 1", "Assignee 2")));
        expectedResponses.add(new ProjectDTO("Project 2", 2L, Arrays.asList("Assignee 3", "Assignee 4")));

        // Mock the projectService to return the sample projects
        when(projectService.getProjectsByManagerId(managerId)).thenReturn(sampleProjects);

        // Call the method being tested
        ResponseEntity<?> responseEntity = projectController.getProjectsByManager(managerId);

        assertNotNull(responseEntity.getBody());
        List<ProjectDTO> actualResponses = (List<ProjectDTO>) responseEntity.getBody();
        assertEquals(expectedResponses.size(), actualResponses.size());
    }

    @Test
    public void testUpdateProject_ValidId() {
        // Assuming projectService, projectRepository, and employeeRepository are properly initialized

        Project sampleProject = new Project();
        sampleProject.setId(1L);
        sampleProject.setTitle("Sample Title");
        sampleProject.setManagerId(1L);
        sampleProject.setStatus("Open");
        projectRepository.save(sampleProject);
        Employee sampleEmployee = new Employee();
        sampleEmployee.setId(1L);

        // Add the employee to the project
        List<Employee> employees = new ArrayList<>();
        employees.add(sampleEmployee);
        sampleProject.setEmployee(employees);

        // Save the sample project to the repository
        projectRepository.save(sampleProject);

        // Create an updated project
        Project updatedProject = new Project();
        updatedProject.setTitle("Updated Title");
        updatedProject.setManagerId(2L);
        updatedProject.setStatus("Closed");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1L);

        List<Employee> updatedEmployees = new ArrayList<>();
        updatedEmployees.add(updatedEmployee);
        updatedProject.setEmployee(updatedEmployees);

        // Call the updateProject method
        ResponseEntity<String> responseEntity = projectController.updateProject(1L, updatedProject);

        // Verify the response
        assertEquals("Project with ID 1 does not exist", responseEntity.getBody());
    }


    @Test
    public void testDeleteProject() {
        Long projectId = 1L;

        Project testProject = new Project();
        testProject.setId(projectId);
        when(projectService.getProjectById(projectId)).thenReturn(testProject);

        ResponseEntity<String> response = projectController.deleteProject(projectId);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Project with ID " + projectId + " deleted", response.getBody());
    }



}



