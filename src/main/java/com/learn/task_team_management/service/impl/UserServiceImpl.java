package com.learn.task_team_management.service.impl;

import com.learn.task_team_management.dto.CreateUserRequest;
import com.learn.task_team_management.dto.UpdateUserRequest;
import com.learn.task_team_management.dto.UserResponse;
import com.learn.task_team_management.entity.User;
import com.learn.task_team_management.enums.UserStatus;
import com.learn.task_team_management.repository.UserRepository;
import com.learn.task_team_management.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus() != null
                ? request.getStatus()
                : UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse updateUser(Long userId, UpdateUserRequest request){
           User user = userRepository.findById(userId)
                   .orElseThrow(() -> new EntityNotFoundException("User not found"));
           user.setName(request.getName());
           user.setRole(request.getRole());
           user.setStatus(request.getStatus());

           return  mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }

        userRepository.deleteById(userId);
    }


    private UserResponse mapToResponse(User user) {

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}
