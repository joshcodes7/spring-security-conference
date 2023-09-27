package com.example.EmployeeManagementApplication.repository;

import com.example.EmployeeManagementApplication.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

//    @Query("DELETE FROM Employee_Project ep WHERE ep.project.id = :projectId")
//    void removeProjectFromEmployeeProject(@Param("projectId") Long projectId);

}
