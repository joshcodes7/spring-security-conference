package com.example.EmployeeManagementApplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
@Getter
//@NoArgsConstructor
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "managerId")
    private Long managerId;

    @Column(name = "status")
    private String status;


    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "employee_project", joinColumns = {
            @JoinColumn(name = "project_id", referencedColumnName = "id")
    },
            inverseJoinColumns = {
                    @JoinColumn(name = "employee_id",referencedColumnName = "id")
            }
    )
    private List<Employee> employee;

    public Project(long id, String title) {
        this.id = id;
        this.title=title;
    }

    public Project(Long managerId) {
        this.managerId = managerId;
    }

    public Project() {
        this.employee = new ArrayList<>();
    }
}
