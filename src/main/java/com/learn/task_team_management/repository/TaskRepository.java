package com.learn.task_team_management.repository;

import com.learn.task_team_management.entity.Task;
import com.learn.task_team_management.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByAssignedUser_Id(Long userId);

    List<Task> findByProject_Id(Long projectId);
}
