package com.learn.task_team_management.service;

import com.learn.task_team_management.dto.CreateTaskRequest;
import com.learn.task_team_management.dto.TaskResponse;
import com.learn.task_team_management.dto.UpdateTaskRequest;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(CreateTaskRequest request);

    TaskResponse updateTask(Long taskId, UpdateTaskRequest request);

    TaskResponse getTaskById(Long taskId);

    List<TaskResponse> getAllTasks();

    List<TaskResponse> getTasksByUser(Long userId);

    List<TaskResponse> getTaskByProject(Long projectId);

    void deleteTask(Long taskId);
}
