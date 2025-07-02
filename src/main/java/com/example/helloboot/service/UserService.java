
package com.example.helloboot.service;

import com.example.helloboot.dto.*;
import com.example.helloboot.entity.*;
import com.example.helloboot.repository.*;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository repo;
    private final SecureRandom random = new SecureRandom();

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    // Generate Token (for login)
    private String generateToken() {
        byte[] bytes = new byte[40];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // Login
    public LoginResponse login(LoginRequest req) {
        return repo.findByUsername(req.getUsername())
                .filter(u -> encoder.matches(req.getPassword(), u.getPassword()))
                .map(u -> {
                    if (!u.isStatus()) {
                        return new LoginResponse(
                                false,
                                "Login Disabled, Contact Admin",
                                null,
                                null,
                                null);
                    }
                    String token = generateToken();
                    u.setToken(token);
                    repo.save(u);
                    return new LoginResponse(
                            true,
                            "Login Successful, Welcome User",
                            token,
                            u.isAdmin(),
                            u.getPermission());
                })
                .orElseGet(() -> new LoginResponse(
                        false,
                        "Incorrect Credential, Try Again",
                        null,
                        null,
                        null));
    }
    public LoginResponse login2(LoginRequest req) {
        return repo.findByName(req.getName())
                .filter(u -> encoder.matches(req.getPassword(), u.getPassword()))
                .map(u -> {
                    if (!u.isStatus()) {
                        return new LoginResponse(
                                false,
                                "Login Disabled, Contact Admin",
                                null,
                                null,
                                null);
                    }
                    String token = generateToken();
                    u.setToken(token);
                    repo.save(u);
                    return new LoginResponse(
                            true,
                            "Login Successful, Welcome User",
                            token,
                            u.isAdmin(),
                            u.getPermission());
                })
                .orElseGet(() -> new LoginResponse(
                        false,
                        "Incorrect Credential, Try Again",
                        null,
                        null,
                        null));
    }

    // Logout
    public GenericResponse logout(String token) {
        if (token == null || token.isBlank())
            return new GenericResponse(
                    false,
                    "Session Expired, Try Again",
                    null,
                    null);
        return repo.findByToken(token)
                .map(u -> {
                    repo.save(u);
                    return new GenericResponse(
                            true,
                            "Logged Out Successfully",
                            null,
                            null);
                })
                .orElseGet(() -> new GenericResponse(
                        false,
                        "Session Expired",
                        null,
                        null));
    }

    // Check
    public GenericResponse check(String token) {
        if (token == null || token.isBlank())
            return new GenericResponse(
                    false,
                    "No Token Provided, Session Expired",
                    null,
                    null);
        Optional<User> userOpt = repo.findByToken(token);
        if (userOpt.isEmpty() || !userOpt.get().isStatus()) {
            return new GenericResponse(
                    false,
                    userOpt.isPresent() ? "Login Disabled, Contact Admin" : "Session Expired, Try Again",
                    null,
                    null);
        }
        User user = userOpt.get();
        return new GenericResponse(
                true,
                "Session Authenticated, Welcome Back",
                user.isAdmin(),
                user.getPermission());
    }

    // Fetch all users (for admin)
    public List<UserDto> getAllUsers() {
        return repo.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    // Fetch a user by ID (for admin)
    public UserDto getUserById(Long id) {
        return repo.findById(id)
                .map(UserDto::fromEntity)
                .orElse(null);
    }

    // Create a new user (for admin)
    public UserDto createUser(UserDto userDto) {
        if (repo.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setPermission(userDto.getPermission());
        user.setStatus(userDto.isStatus());
        user.setPassword(encoder.encode(userDto.getPassword()));
        User saved = repo.save(user);
        return UserDto.fromEntity(saved);
    }

    // Enable/disable login (for admin)
    public boolean updateUserStatus(Long userId) {
        try {
            Optional<User> userOpt = repo.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setStatus(!user.isStatus());
                repo.save(user);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error updating user status: " + e.getMessage());
        }
        return false;
    }

    // Update a user (for admin)
    public UserDto updateUser(Long id, UserDto userDto) {
        return repo.findById(id).map(user -> {
            user.setName(userDto.getName());
            user.setPermission(userDto.getPermission());
            User saved = repo.save(user);
            return UserDto.fromEntity(saved);
        }).orElse(null);
    }

    // Delete a user (for admin)
    public boolean deleteUser(Long id) {
        if (!repo.existsById(id))
            return false;
        repo.deleteById(id);
        return true;
    }
}