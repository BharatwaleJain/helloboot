package com.example.helloboot.service;
import com.example.helloboot.dto.*;
import com.example.helloboot.entity.*;
import com.example.helloboot.repository.*;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class UserService {
    private final UserRepository repo;
    private final SecureRandom random = new SecureRandom();
    public UserService(UserRepository repo) {
        this.repo = repo;
    }
    // Login
    public LoginResponse login(LoginRequest req) {
        return repo.findByUsername(req.getUsername())
            .filter(u -> u.getPassword().equals(req.getPassword()))
            .map(u -> {
                String token = generateToken();
                u.setToken(token);
                repo.save(u);
                return new LoginResponse(
                    true,
                    "Login Successful",
                    token,
                    u.isAdmin(),
                    u.getPermission()
                );
            })
            .orElseGet(() ->
                new LoginResponse(
                    false,
                    "Incorrect Credential, Try Again",
                    null,
                    null,
                    null
                )
            );
    }
    // Logout
    public GenericResponse logout(String token ) {
        if (token == null || token.isBlank())
            return new GenericResponse(
                false,
                "Session Expired",
                null,
                null
            );
        return repo.findByToken(token)
            .map(u -> {
                repo.save(u);
                return new GenericResponse(
                    true,
                    "Logged Out Successfully",
                    null,
                    null
                );
            })
            .orElseGet(() ->
                new GenericResponse(
                    false,
                    "Session Expired",
                    null,
                    null
                )
            );
    }
    // Check
    public GenericResponse check(String token) {
        if (token == null || token.isBlank())
            return new GenericResponse(
                false,
                "No Token Provided",
                null,
                null
            );
        Optional<User> userOpt = repo.findByToken(token);
        boolean valid = userOpt.isPresent();
        return valid ? new GenericResponse(
            true,
            "Session Authenticated",
            userOpt.get().isAdmin(),
            userOpt.get().getPermission()
        ) : new GenericResponse(
            false,
            "Session Expired",
            null,
            null
        );
    }
    // Helper
    private String generateToken() {
        byte[] bytes = new byte[40];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    // Fetch all users (for admin)
    public List<UserDto> getAllUsers() {
        return repo.findAll().stream()
            .map(UserDto::fromEntity)
            .collect(Collectors.toList());
    }
}