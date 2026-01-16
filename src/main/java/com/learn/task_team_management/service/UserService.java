package com.learn.task_team_management.service;

import com.learn.task_team_management.dto.CreateUserRequest;
import com.learn.task_team_management.dto.UpdateUserRequest;
import com.learn.task_team_management.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(Long userId, UpdateUserRequest request);

    UserResponse getUserById(Long userId);

    List<UserResponse> getAllUsers();

    void deleteUser(Long userId);
}
