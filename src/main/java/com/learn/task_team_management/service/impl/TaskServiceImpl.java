package com.learn.task_team_management.service.impl;

import com.learn.task_team_management.dto.CreateTaskRequest;
import com.learn.task_team_management.dto.TaskResponse;
import com.learn.task_team_management.dto.UpdateTaskRequest;
import com.learn.task_team_management.entity.Project;
import com.learn.task_team_management.entity.Task;
import com.learn.task_team_management.entity.User;
import com.learn.task_team_management.repository.ProjectRepository;
import com.learn.task_team_management.repository.TaskRepository;
import com.learn.task_team_management.repository.UserRepository;
import com.learn.task_team_management.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository,
                           ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request){
        User assignedUser = userRepository.findById(request.getAssignedUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Project not found with id: " + request.getProjectId()));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setAssignedUser(assignedUser);
        task.setProject(project);
        task.setDueDate(request.getDueDate());

        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    @Override
    public  TaskResponse updateTask(Long taskId,UpdateTaskRequest request){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        if (request.getAssignedUserId() != null) {
            User user = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            task.setAssignedUser(user);
        }

        return mapToResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        return mapToResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks(){
        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByUser(Long userId){
        return taskRepository.findByAssignedUser_Id(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTaskByProject(Long projectId){
        return taskRepository.findByProject_Id(projectId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long taskId){
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("Task not found");
        }
        taskRepository.deleteById(taskId);
    }

    private TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setProjectId(task.getProject().getId());
        response.setProjectName(task.getProject().getName());

        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());


        if (task.getAssignedUser() != null) {
            response.setAssignedUserId(task.getAssignedUser().getId());
            response.setAssignedUserName(task.getAssignedUser().getName());
        }
        return response;
    }

}
