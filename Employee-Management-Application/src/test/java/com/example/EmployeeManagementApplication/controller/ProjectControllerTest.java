package com.example.EmployeeManagementApplication.controller;

import com.example.EmployeeManagementApplication.dto.ProjectDTO;
import com.example.EmployeeManagementApplication.model.Project;
import com.example.EmployeeManagementApplication.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    private ProjectService projectService = mock(ProjectService.class);
    private ProjectController projectController = new ProjectController();

//    @Test
//    void testGetProject() {
//        Long projectId = 1L;
//        String title = "Sample Project";
//        Long managerId = 100L;
//        List<String> assignees = Arrays.asList("John Doe", "Jane Doe");
//
//        Project project = new Project();
//        project.setId(projectId);
//        project.setTitle(title);
//        project.setManagerId(managerId);
//
//        when(projectService.getProjectById(projectId)).thenReturn(project);
//        when(projectService.getAssigneesNames(projectId)).thenReturn(assignees);
//
//        ResponseEntity<?> responseEntity = projectController.getProject(projectId);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertTrue(responseEntity.getBody() instanceof ProjectDTO);
//
//        ProjectDTO responseDto = (ProjectDTO) responseEntity.getBody();
//        assertEquals(title, responseDto.getTitle());
//        assertEquals(managerId, responseDto.getManagerId());
//        assertEquals(assignees, responseDto.getAssigneeName());
//
//        // Verify that projectService methods were called
//        verify(projectService, times(1)).getProjectById(projectId);
//        verify(projectService, times(1)).getAssigneesNames(projectId);
//    }


}
