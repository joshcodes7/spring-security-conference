package com.example.EmployeeManagementApplication.controller;


import com.example.EmployeeManagementApplication.dto.ProjectDTO;
import com.example.EmployeeManagementApplication.dto.ProjectResponse;
import com.example.EmployeeManagementApplication.model.Employee;
import com.example.EmployeeManagementApplication.model.Project;
import com.example.EmployeeManagementApplication.repository.EmployeeRepository;
import com.example.EmployeeManagementApplication.repository.ProjectRepository;
import com.example.EmployeeManagementApplication.service.EmployeeService;
import com.example.EmployeeManagementApplication.service.ProjectService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = "BearerAuth")
public class ProjectController {


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/project")
    @Transactional
    public ResponseEntity<String> createProject(@RequestBody Project project) {

        Optional<Employee> manager = employeeRepository.findById(project.getManagerId());
        if (!manager.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Manager not found");
        }
        int tc = 0;

        if(!project.getEmployee().isEmpty()) {
            int empcount = project.getEmployee().size();
            for (int i = 0; i < empcount; i++) {
                Employee employee = project.getEmployee().get(i);
                Long eid = employee.getId();
                Optional<Employee> emp = employeeRepository.findById(eid);
                if (emp.isPresent() && eid.equals(emp.get().getId())) {
                    tc++;
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee with ID " + eid + " does not exist.");
                }
            }
            //List<String> projectResponses = new ArrayList<>();
            if (tc == empcount) {
                project.setStatus("todo");
                projectRepository.save(project);
            }
        }
        else{
            project.setStatus("todo");
            projectRepository.save(project);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Project created successfully");
    }

    //@PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/get_project/{id}")
    public ResponseEntity<?> getProject(@PathVariable Long id) {
        // Check if the provided ID is valid
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Invalid project ID");
        }

        Project project = projectService.getProjectById(id);

        if (project == null) {
            return ResponseEntity.status(HttpStatus.OK).body("Project with ID " + id + " does not exist");
        }

        String title = project.getTitle();
        Long managerId = project.getManagerId();
        List<String> assignees = projectService.getAssigneesNames(id);

        // Create a DTO to hold the response data
        ProjectDTO responseDto = new ProjectDTO(title, managerId, assignees);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/project/{id}")
    public ResponseEntity<String> updateProject(@PathVariable Long id, @RequestBody Project project)
    {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Invalid project ID");
        }
        Project project1 = projectService.getProjectById(id);
        if (project1 == null) {
            return ResponseEntity.status(HttpStatus.OK).body("Project with ID " + id + " does not exist");
        }
        else{

            project1.setTitle(project.getTitle());
            project1.setManagerId(project.getManagerId());
            String status = project.getStatus();
            if (status != null && (status.equals("todo") || status.equals("in progress") || status.equals("completed"))) {
                project1.setStatus(status);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid project status");
            }
//            project1.setEmployee(project.getEmployee());

            List<Employee> updatedEmployees = project.getEmployee();
            if (updatedEmployees != null) {
                project1.setEmployee(new ArrayList<>());
                for (Employee employee : updatedEmployees) {
                    Optional<Employee> existingEmployee = employeeRepository.findById(employee.getId());
                    if (existingEmployee.isPresent()) {
                        project1.getEmployee().add(existingEmployee.get());
                    }
                }
            }


            projectRepository.save(project1);

        }
        return ResponseEntity.ok("Project updated successfully");
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {

        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Invalid project ID");
        }

        Project project = projectService.getProjectById(id);

        if (project == null) {
            return ResponseEntity.status(HttpStatus.OK).body("Project with ID " + id + " does not exist");
        }

        List<Employee> employees = project.getEmployee();
        for (Employee employee : employees) {
            employee.getProject().remove(project);
        }
        project.getEmployee().clear();

        projectService.deleteProject(id);

        return ResponseEntity.ok("Project with ID " + id + " deleted");
    }

    @GetMapping("/project/managed/{managerId}")
    public ResponseEntity<?> getProjectsByManager(@PathVariable Long managerId) {

        if (managerId == null || managerId <= 0) {
            return ResponseEntity.badRequest().body("Invalid manager ID");
        }

        List<Project> projects = projectService.getProjectsByManagerId(managerId);

        if (projects.isEmpty()) {
            return ResponseEntity.ok("No projects found for manager ID " + managerId);
        }

        List<ProjectResponse> projectDTOs = projects.stream()
                .map(project -> new ProjectResponse(project.getId(), project.getTitle()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(projectDTOs);
    }

}
