package com.learn.task_team_management.controller;

import com.learn.task_team_management.dto.auth.AuthResponse;
import com.learn.task_team_management.dto.auth.LoginRequest;
import com.learn.task_team_management.entity.User;
import com.learn.task_team_management.repository.UserRepository;
import com.learn.task_team_management.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid  @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String role = auth.getAuthorities().iterator().next().getAuthority();
        String token = tokenProvider.generateToken(request.getEmail(), role);

        User user = userRepository.findByEmail(request.getEmail()).get();

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getRole().name());
    }
}
