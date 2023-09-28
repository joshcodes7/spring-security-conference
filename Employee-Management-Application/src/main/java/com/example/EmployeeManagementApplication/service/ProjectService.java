package com.example.EmployeeManagementApplication.service;

import com.example.EmployeeManagementApplication.model.Employee;
import com.example.EmployeeManagementApplication.model.Project;
import com.example.EmployeeManagementApplication.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<String> getAssigneesNames(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);

        if (project != null) {
            List<Employee> assignees = project.getEmployee();
            List<String> assigneeNames = new ArrayList<>();

            for (Employee assignee : assignees) {
                String fullName = assignee.getFirstName() + " " + assignee.getLastName();
                assigneeNames.add(fullName);
            }

            return assigneeNames;
        }

        return Collections.emptyList();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

//    public void removeProjectFromEmployeeProject(Long projectId) {
//        projectRepository.removeProjectFromEmployeeProject(projectId);
//    }

    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    public List<Project> getProjectsByManagerId(Long managerId) {
        // Assuming ProjectRepository has a method to find projects by managerId
        return projectRepository.findByManagerId(managerId);
    }




}
