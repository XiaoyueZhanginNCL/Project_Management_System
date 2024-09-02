package com.xiaoyue.projectmanagement.repository;

import com.xiaoyue.projectmanagement.model.Project;
import com.xiaoyue.projectmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    List<Project> findByOwner(User owner);

    List<Project> findByNameContainingAndTeamContaining(String partialName,User user);

    @Query("select p from Project p join p.team t where t=:user")
    List<Project> findProjectByTeam(@Param("user")User user);

    List<Project> findByTeamContainingOrOwner(User user,User owner);
}
