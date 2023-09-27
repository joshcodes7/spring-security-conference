package com.example.EmployeeManagementApplication.dto;

import com.example.EmployeeManagementApplication.model.Employee;

import java.util.List;

public class ProjectDTO {

    private String title;
    private Long managerId;
    private List<String> assigneeName;

    public ProjectDTO() {
    }


    public ProjectDTO(String title, Long managerId, List<String> assigneeName) {
        this.title = title;
        this.managerId = managerId;
        this.assigneeName = assigneeName;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }


    public List<String> getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(List<String> assigneeName) {
        this.assigneeName = assigneeName;
    }
}
