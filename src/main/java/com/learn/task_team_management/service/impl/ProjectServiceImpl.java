package com.learn.task_team_management.service.impl;

import com.learn.task_team_management.dto.project.CreateProjectRequest;
import com.learn.task_team_management.dto.project.ProjectResponse;
import com.learn.task_team_management.entity.Project;
import com.learn.task_team_management.entity.User;
import com.learn.task_team_management.repository.ProjectRepository;
import com.learn.task_team_management.repository.UserRepository;
import com.learn.task_team_management.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository,UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProjectResponse createProject(CreateProjectRequest request) {
        if(projectRepository.existsByName(request.getName())){
            throw new IllegalArgumentException("Project name already exists");
        }

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        Set<User> users = new HashSet<>();
        for(Long userId : request.getUserIds()){
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            users.add(user);
        }
        project.setUsers(users);

        Project savedProject = projectRepository.save(project);
        return mapToResponse(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Project not found with id: " + projectId));

        return mapToResponse(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }

        return projectRepository.findByUsers_Id(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Project not found with id: " + projectId);
        }
        projectRepository.deleteById(projectId);
    }

    private ProjectResponse mapToResponse(Project project) {

        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setStatus(project.getStatus().name());
        response.setStartDate(project.getStartDate());
        response.setEndDate(project.getEndDate());

        response.setUserIds(
                project.getUsers()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
        );

        response.setTaskIds(
                project.getTasks()
                        .stream()
                        .map(task -> task.getId())
                        .collect(Collectors.toSet())
        );

        return response;
    }

}
