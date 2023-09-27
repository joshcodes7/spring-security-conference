package com.example.EmployeeManagementApplication.dto;


import java.util.List;

public class EmployeeDTO {

    private String firstName;
    private String lastName;
    private List<String> title;

    public EmployeeDTO(String firstName, String lastName, List<String> title) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

}
