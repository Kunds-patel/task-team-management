package com.learn.task_team_management.service;

import com.learn.task_team_management.dto.user.CreateUserRequest;
import com.learn.task_team_management.dto.user.UpdateUserRequest;
import com.learn.task_team_management.dto.user.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(Long userId, UpdateUserRequest request);

    UserResponse getUserById(Long userId);

    List<UserResponse> getAllUsers();

    void deleteUser(Long userId);
}
