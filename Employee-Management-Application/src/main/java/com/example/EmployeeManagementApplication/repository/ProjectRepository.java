package com.example.EmployeeManagementApplication.repository;

import com.example.EmployeeManagementApplication.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

//    @Query("DELETE FROM Employee_Project ep WHERE ep.project.id = :projectId")
//    void removeProjectFromEmployeeProject(@Param("projectId") Long projectId);

    List<Project> findByManagerId(Long managerId);

}
