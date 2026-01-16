package com.learn.task_team_management.service;

import com.learn.task_team_management.dto.CreateProjectRequest;
import com.learn.task_team_management.dto.ProjectResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse createProject(CreateProjectRequest request);

    ProjectResponse getProjectById(Long projectId);

    List<ProjectResponse> getAllProjects();

    List<ProjectResponse> getProjectsByUserId(Long userId);

    void deleteProject(Long projectId);
}
